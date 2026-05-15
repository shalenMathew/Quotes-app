package com.shalenmathew.quotesapp.presentation.workmanager.notification

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.util.Constants
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.Resource
import com.shalenmathew.quotesapp.util.createOrUpdateNotification
import com.shalenmathew.quotesapp.util.createNotificationChannel
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getNotificationSource
import com.shalenmathew.quotesapp.util.getSavedNotificationQuote
import com.shalenmathew.quotesapp.util.saveNotificationQuote
import com.shalenmathew.quotesapp.util.setLastNotificationAlarmTriggerMillis
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filter
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

            val notificationSource = context.getNotificationSource().first()
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "fetchQuotes: Current source is $notificationSource")

            val quote = when (notificationSource) {
                "favorites" -> getRandomLikedQuote() ?: fetchQuoteFromNetwork() ?: quoteUseCase.getLatestQuote()
                "custom" -> getRandomCustomQuote() ?: getRandomLikedQuote() ?: fetchQuoteFromNetwork() ?: quoteUseCase.getLatestQuote()
                "network" -> fetchQuoteFromNetwork() ?: getRandomLikedQuote() ?: quoteUseCase.getLatestQuote()
                else -> getRandomLikedQuote() ?: fetchQuoteFromNetwork() ?: quoteUseCase.getLatestQuote()
            }

            if (quote != null) {
                Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Fetched Quote: $quote")
                context.saveNotificationQuote(quote)
                true
            } else {
                Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Quote is null")
                false
            }

        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in fetchQuotes", e)
            false
        }

    }

    internal suspend fun getRandomLikedQuote(): Quote? {
        return try {
            val quotes = quoteUseCase.getLikedQuotes().first()
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "getRandomLikedQuote: Found ${quotes.size} liked quotes")
            quotes.shuffled().firstOrNull()
        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in get Random Liked Quote", e)
            null
        }
    }

    internal suspend fun getRandomCustomQuote(): Quote? {
        return try {
            val quotes = customQuoteUseCases.getCustomQuotes("").first()
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "getRandomCustomQuote: Found ${quotes.size} custom quotes")
            quotes.shuffled().firstOrNull()?.let { customQuote ->
                Quote(
                    id = customQuote.id + 100000, // Offset to avoid ID collision with regular quotes
                    quote = customQuote.quote,
                    author = customQuote.author,
                    liked = false
                )
            }
        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in get Random Custom Quote", e)
            null
        }
    }

    private suspend fun fetchQuoteFromNetwork(): Quote? {
        return try {
            val response = withTimeoutOrNull(15000) {
                quoteUseCase.getRandomQuoteFromNetwork()
            }

            when (response) {
                is Resource.Success -> {
                    response.data
                }
                is Resource.Error -> {
                    Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Error from fetchQuoteFromNetwork: ${response.message}")
                    null
                }
                else -> {
                    Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "No response from fetchQuoteFromNetwork")
                    null
                }
            }
        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in fetchQuoteFromNetwork", e)
            null
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): NotificationWorkManager
    }

}