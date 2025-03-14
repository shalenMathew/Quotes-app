package com.example.quotesapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("quote_prefs")


val QUOTE_KEY = stringPreferencesKey("quote")


suspend fun Context.saveQuote(quote: String) {
    dataStore.edit { preferences ->
        preferences[QUOTE_KEY] = quote
    }
}


fun Context.getSavedQuote(): Flow<String> {
    return dataStore.data.map { preferences ->
        preferences[QUOTE_KEY] ?: "No quote saved yet"
    }
}
