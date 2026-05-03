package com.shalenmathew.quotesapp.presentation.workmanager.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.shalenmathew.quotesapp.presentation.receivers.NotificationRefreshReceiver
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.Constants.QUOTES_NOTIFICATION
import com.shalenmathew.quotesapp.util.Constants.REQUEST_CODE_NOTIFICATION_REFRESH
import com.shalenmathew.quotesapp.util.NotificationMode
import com.shalenmathew.quotesapp.util.getLastNotificationAlarmTriggerMillis
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getNotificationDailyTime
import com.shalenmathew.quotesapp.util.getNotificationInterval
import com.shalenmathew.quotesapp.util.getNotificationMode
import com.shalenmathew.quotesapp.util.nextDailyTriggerMillis
import com.shalenmathew.quotesapp.util.setLastNotificationAlarmTriggerMillis
import com.shalenmathew.quotesapp.util.setNotificationDailyTime
import com.shalenmathew.quotesapp.util.setNotificationInterval
import com.shalenmathew.quotesapp.util.setNotificationMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleNotification @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    // This is an app-lifetime scope so persistence survives if the caller (Composable / ViewModel)
    // is torn down between user action and DataStore commit completing.
    private val appScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun applyFrequencyMode(intervalHours: Int) {
        appScope.launch {
            context.setNotificationInterval(intervalHours)
            context.setNotificationMode(NotificationMode.FREQUENCY)

            context.setLastNotificationAlarmTriggerMillis(System.currentTimeMillis())
            scheduleNotificationWorkAlarm(getMillisFromNow(intervalHours))
        }
    }

    fun applyDailyMode(hour: Int, minute: Int) {
        appScope.launch {
            context.setNotificationDailyTime(hour, minute)
            context.setNotificationMode(NotificationMode.DAILY_TIME)

            context.setLastNotificationAlarmTriggerMillis(System.currentTimeMillis())
            scheduleNotificationWorkAlarm(nextDailyTriggerMillis(hour, minute))
        }
    }

    suspend fun scheduleNotification() {

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorkManager>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            ).build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            QUOTES_NOTIFICATION,
            ExistingWorkPolicy.KEEP,
            workRequest
        )

        context.setLastNotificationAlarmTriggerMillis(System.currentTimeMillis())
    }

    fun scheduleNotificationWorkAlarm(triggerAtMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_NOTIFICATION_REFRESH,
            Intent(context, NotificationRefreshReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    suspend fun rescheduleNext() {
        scheduleNotificationWorkAlarm(computeNextTriggerMillis())
    }

    private suspend fun computeNextTriggerMillis(): Long {
        return when (context.getNotificationMode().first()) {
            NotificationMode.DAILY_TIME -> {
                val daily = context.getNotificationDailyTime().first()
                if (daily != null) {
                    nextDailyTriggerMillis(daily.first, daily.second)
                } else {
                    nextFrequencyTriggerMillis()
                }
            }
            NotificationMode.FREQUENCY -> nextFrequencyTriggerMillis()
        }
    }

    private suspend fun nextFrequencyTriggerMillis(): Long {
        val interval = context.getNotificationInterval().first() ?: DEFAULT_REFRESH_INTERVAL
        val intervalMillis = TimeUnit.HOURS.toMillis(interval.toLong())
        val now = System.currentTimeMillis()
        val lastTrigger = context.getLastNotificationAlarmTriggerMillis().first()
        val candidate = lastTrigger + intervalMillis
        return if (candidate > now) candidate else now + intervalMillis
    }

}
