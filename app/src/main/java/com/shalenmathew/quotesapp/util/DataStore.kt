package com.shalenmathew.quotesapp.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("quote_prefs")

val WIDGET_QUOTE_KEY = stringPreferencesKey("widgetQuote")
val WIDGET_QUOTE_ID_KEY = intPreferencesKey("widgetQuoteId")
val WIDGET_QUOTE_LIKED_KEY = booleanPreferencesKey("widgetQuoteLiked")
val NOTIFICATION_QUOTE_MODEL = stringPreferencesKey("notificationQuoteModel")

val IS_FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")
val LAST_ALARM_SET_MILLIS_KEY = longPreferencesKey("last_alarm_set_millis")
val LAST_ALARM_SET_FOR_NOTIFICATION_MILLIS_KEY =
    longPreferencesKey("last_alarm_set_for_notification_millis")
val USER_PREF_WIDGET_REFRESH_INTERVAL_KEY = intPreferencesKey("user_pref_widget_refresh_interval")
val USER_PREF_NOTIFICATION_INTERVAL_KEY = intPreferencesKey("user_pref_notification_interval")

suspend fun Context.setFirstLaunchDone() {
    dataStore.edit { prefs ->
        prefs[IS_FIRST_LAUNCH_KEY] = false
    }
}

fun Context.isFirstLaunch(): Flow<Boolean> {
    return dataStore.data.map { prefs ->
        prefs[IS_FIRST_LAUNCH_KEY] ?: true
    }
}

suspend fun Context.saveNotificationQuote(quote: Quote) {
    dataStore.edit { preferences ->
        preferences[NOTIFICATION_QUOTE_MODEL] = Gson().toJson(quote)
    }
}

fun Context.getSavedNotificationQuote(): Flow<Quote?> {
    return dataStore.data.map { preferences ->
        if (preferences[NOTIFICATION_QUOTE_MODEL].isNullOrEmpty()) {
            return@map null
        } else {
            return@map Gson().fromJson(preferences[NOTIFICATION_QUOTE_MODEL], Quote::class.java)
        }
    }
}

fun Context.getWidgetRefreshInterval(): Flow<Int?> {
    return dataStore.data.map { preferences ->
        preferences[USER_PREF_WIDGET_REFRESH_INTERVAL_KEY]
    }
}

suspend fun Context.setWidgetRefreshInterval(interval: Int) {
    dataStore.edit { preferences ->
        preferences[USER_PREF_WIDGET_REFRESH_INTERVAL_KEY] = interval
    }
}

fun Context.getNotificationInterval(): Flow<Int?> {
    return dataStore.data.map { preferences ->
        preferences[USER_PREF_NOTIFICATION_INTERVAL_KEY]
    }
}

suspend fun Context.setNotificationInterval(interval: Int) {
    dataStore.edit { preferences ->
        preferences[USER_PREF_NOTIFICATION_INTERVAL_KEY] = interval
    }
}

suspend fun Context.setLastAlarmTriggerMillis(interval: Long) {
    dataStore.edit { preferences ->
        preferences[LAST_ALARM_SET_MILLIS_KEY] = interval
    }
}

fun Context.getLastAlarmTriggerMillis(): Flow<Long> {
    return dataStore.data.map { preferences ->
        preferences[LAST_ALARM_SET_MILLIS_KEY] ?: System.currentTimeMillis()
    }
}

suspend fun Context.setLastNotificationAlarmTriggerMillis(interval: Long) {
    dataStore.edit { preferences ->
        preferences[LAST_ALARM_SET_FOR_NOTIFICATION_MILLIS_KEY] = interval
    }
}

fun Context.getLastNotificationAlarmTriggerMillis(): Flow<Long> {
    return dataStore.data.map { preferences ->
        preferences[LAST_ALARM_SET_FOR_NOTIFICATION_MILLIS_KEY] ?: System.currentTimeMillis()
    }
}
suspend fun Context.isWidgetCacheStale(refreshInterval: Int): Boolean {
    val lastTrigger = getLastAlarmTriggerMillis().first()
    val now = System.currentTimeMillis()
    return lastTrigger == 0L || (now - lastTrigger >= refreshInterval * 60_000L)
}