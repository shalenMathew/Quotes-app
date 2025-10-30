package com.shalenmathew.quotesapp.data.local

import android.content.SharedPreferences
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle
import androidx.core.content.edit
import jakarta.inject.Inject

class DefaultQuoteStylePreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): DefaultQuoteStylePreferences {

    override fun saveDefaultQuoteStyle(quoteStyle: QuoteStyle) {
        val quoteStyleString = when (quoteStyle) {
            is QuoteStyle.DefaultTheme -> "DefaultTheme"
            is QuoteStyle.CodeSnippetTheme -> "CodeSnippetTheme"
            is QuoteStyle.DiceDreamsTheme -> "DiceDreamsTheme"
            is QuoteStyle.LiquidGlassTheme -> "LiquidGlassTheme"
//            is QuoteStyle.SpotifyTheme -> "SpotifyTheme"
            is QuoteStyle.bratTheme -> "bratTheme"
            is QuoteStyle.igorTheme -> "igorTheme"
            QuoteStyle.ReminderTheme -> "ReminderTheme"
        }
        sharedPreferences.edit {
            putString(QUOTE_STYLE_KEY, quoteStyleString)
        }
    }

    override fun getDefaultQuoteStyle(): QuoteStyle {
        return when (sharedPreferences.getString(QUOTE_STYLE_KEY, "DefaultTheme")) {
            "CodeSnippetTheme" -> QuoteStyle.CodeSnippetTheme
            "DiceDreamsTheme" -> QuoteStyle.DiceDreamsTheme
//            "SpotifyTheme" -> QuoteStyle.SpotifyTheme
            "bratTheme" -> QuoteStyle.bratTheme
            "igorTheme" -> QuoteStyle.igorTheme
            "LiquidGlassTheme" -> QuoteStyle.LiquidGlassTheme
            "ReminderTheme" -> QuoteStyle.ReminderTheme
            else -> QuoteStyle.DefaultTheme
        }
    }

    companion object {
        private const val QUOTE_STYLE_KEY = "quote_style_key"
    }
}