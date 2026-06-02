package com.shalenmathew.quotesapp.presentation.workmanager.notification

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.util.Constants
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.Resource
import com.shalenmathew.quotesapp.util.createOrUpdateNotification
import com.shalenmathew.quotesapp.util.createNotificationChannel
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getSavedNotificationQuote
import com.shalenmathew.quotesapp.util.getNotificationSources
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.toQuote
import com.shalenmathew.quotesapp.util.saveNotificationQuote
import com.shalenmathew.quotesapp.util.setLastNotificationAlarmTriggerMillis
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

@HiltWorker
class NotificationWorkManager @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val quoteUseCase: QuoteUseCase,
    private val customQuoteUseCases: CustomQuoteUseCases,
    private val scheduleNotification: ScheduleNotification
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Work started")
            val response = fetchQuotes(context)
            if (!response) {
                return Result.retry()
            }
            val quote = context.getSavedNotificationQuote().first()

            Log.d(
                Constants.WORK_MANAGER_STATUS_NOTIFY,
                "Saved Quote in DataStore work manager: $quote"
            )

            context.createNotificationChannel()
            quote?.let {
                context.createOrUpdateNotification(it)
            }

            context.setLastNotificationAlarmTriggerMillis(System.currentTimeMillis())

            Result.success()
        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in doWork", e)
            Result.failure()
        }
    }

    private suspend fun fetchQuotes(context: Context): Boolean {

        return try {

            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "inside fetchQuotes")

            try {
                if (withTimeoutOrNull(5000) { scheduleNotification.rescheduleNext() } == null) {
                    Log.w(
                        Constants.WORK_MANAGER_STATUS_NOTIFY,
                        "rescheduleNext timed out; using default-interval fallback"
                    )
                    scheduleNotification.scheduleNotificationWorkAlarm(
                        getMillisFromNow(DEFAULT_REFRESH_INTERVAL)
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    Constants.WORK_MANAGER_STATUS_NOTIFY,
                    "rescheduleNext failed; using default-interval fallback",
                    e
                )
                scheduleNotification.scheduleNotificationWorkAlarm(
                    getMillisFromNow(DEFAULT_REFRESH_INTERVAL)
                )
            }
            
            val enabledSources = context.getNotificationSources().first().toList()
            if (enabledSources.isEmpty()) {
                Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "No notification sources configured")
                return false
            }

            val selectedSource = enabledSources.random()
            Log.d(
                Constants.WORK_MANAGER_STATUS_NOTIFY,
                "Selected notification source for this refresh: $selectedSource (enabled: $enabledSources)"
            )

            val finalQuote = quoteFromNotificationSource(selectedSource)

            if (finalQuote != null) {
                Log.d(
                    Constants.WORK_MANAGER_STATUS_NOTIFY,
                    "Fetched quote from notification source $selectedSource: $finalQuote"
                )
                context.saveNotificationQuote(finalQuote)
                true
            } else {
                Log.d(
                    Constants.WORK_MANAGER_STATUS_NOTIFY,
                    "Notification source $selectedSource returned no quote; will retry"
                )
                false
            }

        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in fetchQuotes", e)
            false
        }

    }

    private suspend fun quoteFromNotificationSource(source: String): Quote? {
        return when (source) {
            "network" -> fetchQuoteFromNetwork()
            "favourite" -> getRandomLikedQuote()
            "custom" -> getRandomCustomQuote()
            else -> null
        }
    }

    private suspend fun fetchQuoteFromNetwork(): Quote? {
        return try {
            when (val response = withTimeoutOrNull(NETWORK_TIMEOUT_MILLIS) {
                quoteUseCase.getRandomRemoteQuote()
            }) {
                is Resource.Success -> response.data
                is Resource.Error -> {
                    Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Network error: ${response.message}")
                    null
                }
                null -> {
                    Log.d(
                        Constants.WORK_MANAGER_STATUS_NOTIFY,
                        "Network fetch timed out after ${NETWORK_TIMEOUT_MILLIS}ms"
                    )
                    null
                }
                else -> null
            }
        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in fetchQuoteFromNetwork", e)
            null
        }
    }

    private suspend fun getRandomLikedQuote(): Quote? {
        return try {
            val quotes = quoteUseCase.getLikedQuotes().first()
            quotes.shuffled().firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getRandomCustomQuote(): Quote? {
        return try {
            val quotes = customQuoteUseCases.getCustomQuotes("").first()
            quotes.shuffled().firstOrNull()?.toQuote()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val NETWORK_TIMEOUT_MILLIS = 10_000L
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): NotificationWorkManager
    }

}