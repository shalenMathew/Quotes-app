package com.shalenmathew.quotesapp.presentation.workmanager.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.shalenmathew.quotesapp.presentation.receivers.WidgetRefreshReceiver
import com.shalenmathew.quotesapp.util.Constants.QUOTES_WIDGET_UPDATE_NAME
import com.shalenmathew.quotesapp.util.Constants.REQUEST_CODE_ALARM_WIDGET_REFRESH
import com.shalenmathew.quotesapp.util.setLastAlarmTriggerMillis
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleWidgetRefresh @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    suspend fun scheduleWidgetRefreshWorkManager() {
        val workRequest = OneTimeWorkRequestBuilder<WidgetWorkManager>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            QUOTES_WIDGET_UPDATE_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest
        )

        context.setLastAlarmTriggerMillis(System.currentTimeMillis())
    }

    fun scheduleWidgetRefreshWorkAlarm(triggerAtMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM_WIDGET_REFRESH,
            Intent(context, WidgetRefreshReceiver::class.java),
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