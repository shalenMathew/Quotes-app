package com.shalenmathew.quotesapp.util

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shalenmathew.quotesapp.presentation.widget.QuotesWidgetReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("quote_prefs")

val WIDGET_QUOTE_KEY = stringPreferencesKey("widgetQuote")
val NOTIFICATION_QUOTE_KEY = stringPreferencesKey("notificationQuote")
val NOTIFICATION_AUTHOR_KEY = stringPreferencesKey("notificationAuthor")

val IS_FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")

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

suspend fun Context.saveWidgetQuote(quote: String) {
    // saving quote of the day
    dataStore.edit { preferences ->
        preferences[WIDGET_QUOTE_KEY] = quote
    }

    val intent = Intent(this, QuotesWidgetReceiver::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    }

    sendBroadcast(intent)
}

suspend fun Context.saveNotificationQuote(quote: String,author: String){
    dataStore.edit { preferences ->
        preferences[NOTIFICATION_QUOTE_KEY] = quote
        preferences[NOTIFICATION_AUTHOR_KEY] = author
    }
}

fun Context.getSavedNotificationQuote(): Flow<String> {
    return dataStore.data.map { preferences ->
        preferences[NOTIFICATION_QUOTE_KEY] ?: "No quote saved yet..."
        preferences[NOTIFICATION_AUTHOR_KEY] ?: "No author saved yet..."
    }
}


fun Context.getSavedWidgetQuote(): Flow<String> {
    return dataStore.data.map { preferences ->
        preferences[WIDGET_QUOTE_KEY] ?: "No quote saved yet..."
    }
}
