package com.shalenmathew.quotesapp.presentation.workmanager.notification

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.util.Constants
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.Resource
import com.shalenmathew.quotesapp.util.createOrUpdateNotification
import com.shalenmathew.quotesapp.util.createNotificationChannel
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getNotificationInterval
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

            val response =
                withTimeoutOrNull(5000) {
                    val refreshInterval =
                        context.getNotificationInterval().first() ?: DEFAULT_REFRESH_INTERVAL
                    scheduleNotification.scheduleNotificationWorkAlarm(
                        getMillisFromNow(
                            refreshInterval
                        )
                    )
                    quoteUseCase.getQuote()
                        .filter { it is Resource.Success || it is Resource.Error }
                        .first()
                }
            when (response) {

                is Resource.Success -> {
                    val quote = response.data?.quotesList?.getOrNull(1)

                    if (quote != null) {
                        Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Fetched Quote: $quote")
                        context.saveNotificationQuote(quote)
                        true
                    } else {
                        Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Quote is null")
                        false
                    }

                }

                is Resource.Error -> {
                    Log.d(
                        Constants.WORK_MANAGER_STATUS_NOTIFY,
                        "Error from fetchQuotes: ${response.message}"
                    )
                    false
                }

                else -> {
                    Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "No response from fetchQuotes")
                    false
                }
            }

        } catch (e: Exception) {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Exception in fetchQuotes", e)
            false
        }

    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): NotificationWorkManager
    }

}