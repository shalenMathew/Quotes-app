package com.shalenmathew.quotesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh,
) : ViewModel() {

    fun scheduleWidgetRefreshWorkAlarm(triggerAtMillis : Long) {
        scheduleWidgetRefresh.scheduleWidgetRefreshWorkAlarm(triggerAtMillis)
    }

}