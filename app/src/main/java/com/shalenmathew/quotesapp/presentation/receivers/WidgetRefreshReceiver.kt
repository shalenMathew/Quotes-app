package com.shalenmathew.quotesapp.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import com.shalenmathew.quotesapp.util.Constants.DEFAULT_WIDGET_REFRESH_INTERVAL_MINUTES
import com.shalenmathew.quotesapp.util.getLastAlarmTriggerMillis
import com.shalenmathew.quotesapp.util.getMillisFromNowMinutes
import com.shalenmathew.quotesapp.util.getWidgetRefreshIntervalMinutes
import com.shalenmathew.quotesapp.util.setLastAlarmTriggerMillis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class WidgetRefreshReceiver() : BroadcastReceiver() {
    @Inject
    lateinit var scheduleWidgetRefresh: ScheduleWidgetRefresh
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()

        scope.launch {
            try {

                if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                    val lastAlarmSetTimeMillis =
                        context?.getLastAlarmTriggerMillis()?.first() ?: getMillisFromNowMinutes(
                            DEFAULT_WIDGET_REFRESH_INTERVAL_MINUTES
                        )
                    val widgetRefreshIntervalMinutes = context?.getWidgetRefreshIntervalMinutes()?.first()
                        ?: DEFAULT_WIDGET_REFRESH_INTERVAL_MINUTES
                    var timeToTrigger =
                        lastAlarmSetTimeMillis + TimeUnit.MINUTES.toMillis(widgetRefreshIntervalMinutes.toLong())

                    if (timeToTrigger <= System.currentTimeMillis()) {
                        timeToTrigger = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(
                            widgetRefreshIntervalMinutes.toLong()
                        )
                    }

                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(timeToTrigger)
                    context?.setLastAlarmTriggerMillis(timeToTrigger)
                } else {
                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                }

            } catch (e: Exception) {
                Log.e("WidgetRefreshReceiver", "onReceive Exception: ${e.message}")
            } finally {
                pendingResult.finish()
            }
        }

    }


}