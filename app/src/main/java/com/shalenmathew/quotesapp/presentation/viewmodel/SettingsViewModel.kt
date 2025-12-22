package com.shalenmathew.quotesapp.presentation.viewmodel

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

    // Widget refresh scheduling
    fun scheduleWidgetRefreshWorkAlarm(triggerAtMillis: Long) {
        scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(triggerAtMillis)
    }

    // Notification frequency update + scheduling
    fun updateNotificationFrequency(value: String) {

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
