package com.shalenmathew.quotesapp.presentation.workmanager.widget

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetUseCase
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.Resource
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getWidgetRefreshInterval
import com.shalenmathew.quotesapp.util.isWidgetCacheStale
import com.shalenmathew.quotesapp.util.setLastAlarmTriggerMillis
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

@HiltWorker
class WidgetWorkManager @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val quoteUseCase: QuoteUseCase,
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh,
    private val updateWidgetUseCase: UpdateWidgetUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        return try {

            Log.d(TAG, "Work started")



            val refreshInterval =
                context.getWidgetRefreshInterval().first() ?: DEFAULT_WIDGET_REFRESH_INTERVAL
            val isCacheStale = context.isWidgetCacheStale(refreshInterval)

            val success = if (isCacheStale) {
                refreshAndUpdateWidget(refreshInterval)
            } else {
                updateWidgetFromCache()
            }

            if (success && isCacheStale) {
                context.setLastAlarmTriggerMillis(System.currentTimeMillis())
            }

            if (success) Result.success() else Result.retry()
        } catch (e: Exception) {
            Log.d(TAG, "Exception in doWork", e)
            Result.failure()
        }
    }

    private suspend fun refreshAndUpdateWidget(refreshInterval: Int): Boolean {
        scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(getMillisFromNow(refreshInterval))
        val quote = fetchQuoteFromNetwork() ?: quoteUseCase.getLatestQuote()
        return pushQuoteToWidget(quote)
    }

    private suspend fun updateWidgetFromCache(): Boolean {
        Log.d(TAG, "Cache is fresh, reading from local DB")
        return pushQuoteToWidget(quoteUseCase.getLatestQuote())
    }

    private suspend fun fetchQuoteFromNetwork(): Quote? {
        return try {
            val response = withTimeoutOrNull(NETWORK_TIMEOUT_MILLIS) {
                quoteUseCase.getQuote()
                    .filter { it is Resource.Success || it is Resource.Error }
                    .first()
            }

            when (response) {
                is Resource.Success -> {
                    Log.d(TAG, "Fetched quote from network")
                    response.data?.quotesList?.getOrNull(0)
                }

                is Resource.Error -> {
                    Log.d(TAG, "Network error: ${response.message}")
                    null
                }

                else -> {
                    Log.d(TAG, "Network timeout")
                    null
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception in fetchQuoteFromNetwork", e)
            null
        }
    }

    private suspend fun pushQuoteToWidget(quote: Quote?): Boolean {
        if (quote == null) {
            Log.d(TAG, "No quote available")
            return false
        }
        updateWidgetUseCase(quote)
            .onFailure { Log.w(TAG, "Widget update failed: ${it.message}") }
        return true
    }

    companion object {
        private const val TAG = "WidgetWorkManager"
        private const val NETWORK_TIMEOUT_MILLIS = 5000L
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): WidgetWorkManager
    }
}