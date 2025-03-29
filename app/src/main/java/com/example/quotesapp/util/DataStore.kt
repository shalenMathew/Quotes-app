package com.example.quotesapp.util

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.quotesapp.presentation.widget.QuotesWidgetReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("quote_prefs")


val QUOTE_KEY = stringPreferencesKey("quote")


suspend fun Context.saveQuote(quote: String) {
    // saving quote of the day
    dataStore.edit { preferences ->
        preferences[QUOTE_KEY] = quote
    }

    val intent = Intent(this, QuotesWidgetReceiver::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    }

    sendBroadcast(intent)

}


fun Context.getSavedQuote(): Flow<String> {
    return dataStore.data.map { preferences ->
        preferences[QUOTE_KEY] ?: "No quote saved yet..."
    }
}
