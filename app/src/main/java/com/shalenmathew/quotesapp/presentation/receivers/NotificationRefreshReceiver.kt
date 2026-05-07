package com.shalenmathew.quotesapp.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.shalenmathew.quotesapp.presentation.workmanager.notification.ScheduleNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
                    notificationScheduler.rescheduleNext()
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
