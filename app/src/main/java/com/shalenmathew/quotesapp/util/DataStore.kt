package com.shalenmathew.quotesapp.util

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import com.google.gson.Gson
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.presentation.widget.QuotesWidgetObj
import com.shalenmathew.quotesapp.presentation.widget.QuotesWidgetReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("quote_prefs")

val WIDGET_QUOTE_KEY = stringPreferencesKey("widgetQuote")
val NOTIFICATION_QUOTE_KEY = stringPreferencesKey("notificationQuote")
val NOTIFICATION_AUTHOR_KEY = stringPreferencesKey("notificationAuthor")

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

suspend fun Context.saveWidgetQuoteObject(quote: Quote) {
    // saving quote object as JSON
    val gson = Gson()
    val quoteJson = gson.toJson(quote)
    dataStore.edit { preferences ->
        preferences[WIDGET_QUOTE_KEY] = quoteJson
    }

    // Update the widget
    val glanceAppWidgetManager = GlanceAppWidgetManager(this)
    val widgetIds = glanceAppWidgetManager.getGlanceIds(QuotesWidgetObj::class.java)
    if (widgetIds.isNotEmpty()) {
        widgetIds.forEach { id ->
            QuotesWidgetObj.update(this, id)
        }
    }
}

suspend fun Context.saveNotificationQuote(quote: String, author: String) {
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

fun Context.getSavedWidgetQuoteObject(): Flow<Quote?> {
    return dataStore.data.map { preferences ->
        val quoteJson = preferences[WIDGET_QUOTE_KEY]
        if (quoteJson != null) {
            try {
                val gson = Gson()
                gson.fromJson(quoteJson, Quote::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}

suspend fun Context.toggleWidgetQuoteLike() {
    val currentQuote = getSavedWidgetQuoteObject().first()
    if (currentQuote != null) {
        val wasLiked = currentQuote.liked
        val updatedQuote = currentQuote.copy(liked = !currentQuote.liked)
        saveWidgetQuoteObject(updatedQuote)

        // Log the action for debugging (favorites saving is handled by MainActivity)
        if (!wasLiked && updatedQuote.liked) {
            Log.d("WidgetLike", "Quote liked: ${updatedQuote.quote}")
        } else if (wasLiked && !updatedQuote.liked) {
            Log.d("WidgetLike", "Quote unliked: ${updatedQuote.quote}")
        }
    }
}

suspend fun Context.saveQuoteToFavorites(quote: Quote) {
    // This function is no longer used - favorites saving is handled by MainActivity with proper DI
    Log.d("SaveToFavorites", "Deprecated function called for: ${quote.quote} by ${quote.author}")
}
