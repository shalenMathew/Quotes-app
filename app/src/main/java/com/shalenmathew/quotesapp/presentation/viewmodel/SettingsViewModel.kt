package com.shalenmathew.quotesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.shalenmathew.quotesapp.presentation.workmanager.notification.ScheduleNotification
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh,
    private val scheduleNotificationRefresh: ScheduleNotification,
) : ViewModel() {

    fun scheduleWidgetRefreshWorkAlarm(triggerAtMillis: Long) {
        scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(triggerAtMillis)
    }

    fun applyFrequencyMode(intervalHours: Int) {
        scheduleNotificationRefresh.applyFrequencyMode(intervalHours)
    }

    fun applyDailyMode(hour: Int, minute: Int) {
        scheduleNotificationRefresh.applyDailyMode(hour, minute)
    }
}
