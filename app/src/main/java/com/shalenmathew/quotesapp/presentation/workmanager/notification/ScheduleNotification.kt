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
import com.shalenmathew.quotesapp.util.Constants.QUOTES_NOTIFICATION
import com.shalenmathew.quotesapp.util.Constants.REQUEST_CODE_NOTIFICATION_REFRESH
import com.shalenmathew.quotesapp.util.setLastNotificationAlarmTriggerMillis
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleNotification @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

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

}