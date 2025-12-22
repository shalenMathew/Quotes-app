package com.shalenmathew.quotesapp.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shalenmathew.quotesapp.domain.NotificationFrequency
import com.shalenmathew.quotesapp.presentation.workmanager.notification.ScheduleNotification
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh,
    private val scheduleNotification: ScheduleNotification
) : ViewModel() {

    // Default notification schedule (Every 2 Days)
    init {
        scheduleNotification.scheduleNotification(
            intervalInDays = NotificationFrequency.EVERY_2_DAYS.days
        )
    }

    // Widget refresh scheduling
    fun scheduleWidgetRefreshWorkAlarm(triggerAtMillis: Long) {
        scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(triggerAtMillis)
    }

    // Notification frequency state
    private val _notificationFrequency = mutableStateOf("Every 2 Days")
    val notificationFrequency: State<String> get() = _notificationFrequency

    // Update + reschedule notification
    fun updateNotificationFrequency(value: String) {
        _notificationFrequency.value = value

        val frequency = when (value) {
            "Daily" -> NotificationFrequency.DAILY
            "Weekly" -> NotificationFrequency.WEEKLY
            else -> NotificationFrequency.EVERY_2_DAYS
        }

        scheduleNotification.scheduleNotification(
            intervalInDays = frequency.days
        )
    }
}
