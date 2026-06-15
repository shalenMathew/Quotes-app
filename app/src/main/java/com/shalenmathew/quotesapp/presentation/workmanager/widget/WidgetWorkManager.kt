package com.shalenmathew.quotesapp.presentation.workmanager.widget

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetUseCase
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.Resource
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getWidgetRefreshInterval
import com.shalenmathew.quotesapp.util.getWidgetSources
import com.shalenmathew.quotesapp.util.setLastAlarmTriggerMillis
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

@HiltWorker
class WidgetWorkManager @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val quoteUseCase: QuoteUseCase,
    private val customQuoteUseCases: CustomQuoteUseCases,
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh,
    private val updateWidgetUseCase: UpdateWidgetUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {




        /** NETWORK FIRST APPROACH TO UPDATE WIDGET */
//        return try {
//
//            Log.d(TAG, "Work started")

//            val refreshInterval =
//                context.getWidgetRefreshInterval().first() ?: DEFAULT_REFRESH_INTERVAL

//            val isCacheStale = context.isWidgetCacheStale(refreshInterval)
//
//            val success = if (isCacheStale) {
//                refreshAndUpdateWidget(refreshInterval)
//            } else {
//                updateWidgetFromCache()
//            }
//
//            if (success && isCacheStale) {
//                context.setLastAlarmTriggerMillis(System.currentTimeMillis())
//            }
//
//            if (success) Result.success() else Result.retry()
//        } catch (e: Exception) {
//            Log.d(TAG, "Exception in doWork", e)
//            Result.failure()
//        }


        return try {

            /** LIKED FIRST APPROACH TO UPDATE WIDGET */

            val refreshInterval = context.getWidgetRefreshInterval().first() ?: DEFAULT_REFRESH_INTERVAL

            val success = refreshAndUpdateWidget(refreshInterval)

            if (success) {
                context.setLastAlarmTriggerMillis(System.currentTimeMillis())
                Result.success()
            } else {

//                Result.retry()

                Log.d(TAG, "Update failed, skipping this interval to save battery.")
                Result.success()
            }


        } catch (e: Exception) {
            Log.d(TAG, "Exception in doWork", e)
            Result.failure()
        }

    }

    private suspend fun refreshAndUpdateWidget(refreshInterval: Int): Boolean {
        scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(getMillisFromNow(refreshInterval))
        val sources = context.getWidgetSources().first()
        Log.d(TAG, "refreshAndUpdateWidget: Current sources are $sources")
        val quote = fetchQuoteFromSources(allowNetwork = true)
        return pushQuoteToWidget(quote)
    }

//    private suspend fun updateWidgetFromCache(): Boolean {
//        Log.d(TAG, "Cache is fresh, reading from local DB")
//        val widgetSource = context.getWidgetSource().first()
//        val quote = when (widgetSource) {
//            "favorites" -> getRandomLikedQuote() ?: quoteUseCase.getLatestQuote()
//            "custom" -> getRandomCustomQuote() ?: getRandomLikedQuote() ?: quoteUseCase.getLatestQuote()
//            "network" -> getRandomLikedQuote() ?: quoteUseCase.getLatestQuote()
//            else -> getRandomLikedQuote() ?: quoteUseCase.getLatestQuote()
//        }
//        return pushQuoteToWidget(quote)
//    }

    private suspend fun fetchQuoteFromSources(allowNetwork: Boolean): Quote? {
        val enabledSources = context.getWidgetSources().first()
            .filter { source -> source != "network" || allowNetwork }
            .toList()
        if (enabledSources.isEmpty()) {
            Log.w(TAG, "No widget sources enabled")
            return null
        }

        val selectedSource = enabledSources.random()
        Log.d(TAG, "Selected widget source for this refresh: $selectedSource (enabled: $enabledSources)")

        val quote = quoteFromWidgetSource(selectedSource, allowNetwork)
        if (quote != null) {
            Log.d(TAG, "Quote loaded from widget source: $selectedSource")
        } else {
            Log.d(TAG, "Widget source $selectedSource returned no quote; will retry")
        }
        return quote
    }

    private suspend fun quoteFromWidgetSource(source: String, allowNetwork: Boolean): Quote? {
        return when (source) {
            "favorites" -> getRandomLikedQuote()
            "custom" -> getRandomCustomQuote()
            "network" -> if (allowNetwork) fetchQuoteFromNetwork() else null
            else -> null
        }
    }

    internal suspend fun getRandomLikedQuote(): Quote? {
        return try {
            val quotes = quoteUseCase.getLikedQuotes().first()
            Log.d(TAG, "getRandomLikedQuote: Found ${quotes.size} liked quotes")
            quotes.shuffled().firstOrNull()
        } catch (e: Exception) {
            Log.d(TAG, "Exception in get Random Liked Quote", e)
            null
        }

    }

    internal suspend fun getRandomCustomQuote(): Quote? {
        return try {
            val quotes = customQuoteUseCases.getCustomQuotes("").first()
            Log.d(TAG, "getRandomCustomQuote: Found ${quotes.size} custom quotes")
            quotes.shuffled().firstOrNull()?.let { customQuote ->
                Quote(
                    id = customQuote.id + 100000, // Offset to avoid ID collision with regular quotes
                    quote = customQuote.quote,
                    author = customQuote.author,
                    liked = false
                )
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception in get Random Custom Quote", e)
            null
        }
    }
    private suspend fun fetchQuoteFromNetwork(): Quote? {
        return try {
            when (val response = withTimeoutOrNull(NETWORK_TIMEOUT_MILLIS) {
                quoteUseCase.getRandomRemoteQuote()
            }) {
                is Resource.Success -> {
                    Log.d(TAG, "Fetched random quote from network")
                    response.data
                }

                is Resource.Error -> {
                    Log.d(TAG, "Network error: ${response.message}")
                    null
                }

                null -> {
                    Log.d(TAG, "Network fetch timed out after ${NETWORK_TIMEOUT_MILLIS}ms")
                    null
                }

                else -> null
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
        private const val NETWORK_TIMEOUT_MILLIS = 10_000L
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): WidgetWorkManager
    }
}