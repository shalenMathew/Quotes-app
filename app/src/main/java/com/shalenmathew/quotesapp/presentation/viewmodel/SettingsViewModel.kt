package com.shalenmathew.quotesapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalenmathew.quotesapp.presentation.workmanager.notification.ScheduleNotification
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import com.shalenmathew.quotesapp.util.setNotificationSources
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh,
    private val scheduleNotificationRefresh: ScheduleNotification,
) : ViewModel() {

    fun scheduleWidgetRefreshWorkManager() {
        viewModelScope.launch {
            scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
        }
    }

    fun scheduleWidgetRefreshWorkAlarm(triggerAtMillis: Long) {
        scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(triggerAtMillis)
    }

    fun applyFrequencyMode(intervalHours: Int) {
        scheduleNotificationRefresh.applyFrequencyMode(intervalHours)
    }

    fun applyDailyMode(hour: Int, minute: Int) {
        scheduleNotificationRefresh.applyDailyMode(hour, minute)
    }

    fun saveNotificationSources(sources: Set<String>) {
        viewModelScope.launch {
            context.setNotificationSources(sources)
        }
    }
}
