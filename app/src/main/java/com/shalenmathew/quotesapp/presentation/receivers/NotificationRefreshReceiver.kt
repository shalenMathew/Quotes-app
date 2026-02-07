package com.shalenmathew.quotesapp.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.shalenmathew.quotesapp.presentation.workmanager.notification.ScheduleNotification
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_REFRESH_INTERVAL
import com.shalenmathew.quotesapp.util.getLastNotificationAlarmTriggerMillis
import com.shalenmathew.quotesapp.util.getMillisFromNow
import com.shalenmathew.quotesapp.util.getNotificationInterval
import com.shalenmathew.quotesapp.util.setLastNotificationAlarmTriggerMillis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class NotificationRefreshReceiver() : BroadcastReceiver() {
    @Inject
    lateinit var notificationScheduler: ScheduleNotification
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()

        scope.launch {
            try {

                if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                    val lastAlarmSetTimeMillis =
                        context?.getLastNotificationAlarmTriggerMillis()?.first()
                            ?: getMillisFromNow(
                                DEFAULT_REFRESH_INTERVAL
                            )
                    val notificationRefreshInterval = context?.getNotificationInterval()?.first()
                        ?: DEFAULT_REFRESH_INTERVAL
                    var timeToTrigger =
                        lastAlarmSetTimeMillis + TimeUnit.HOURS.toMillis(notificationRefreshInterval.toLong())

                    if (timeToTrigger <= System.currentTimeMillis()) {
                        timeToTrigger = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(
                            notificationRefreshInterval.toLong()
                        )
                    }

                    notificationScheduler.scheduleNotificationWorkAlarm(timeToTrigger)
                    context?.setLastNotificationAlarmTriggerMillis(timeToTrigger)
                } else {
                    notificationScheduler.scheduleNotification()
                }
            } catch (e: Exception) {
                Log.e("NotificationRefreshReceiver", "onReceive Exception: ${e.message}")
            } finally {
                pendingResult.finish()
            }
        }

    }


}