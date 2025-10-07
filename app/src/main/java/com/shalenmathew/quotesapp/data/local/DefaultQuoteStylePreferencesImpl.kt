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
            QuoteStyle.DEFAULT_THEME -> "DefaultTheme"
            QuoteStyle.CODE_SNIPPET_THEME -> "CodeSnippetTheme"
            QuoteStyle.LIQUID_GLASS_THEME -> "LiquidGlassTheme"
            QuoteStyle.BRAT_THEME -> "bratTheme"
            QuoteStyle.IGOR_THEME -> "igorTheme"
            QuoteStyle.REMINDER_THEME -> "ReminderTheme"
        }
        sharedPreferences.edit {
            putString(QUOTE_STYLE_KEY, quoteStyleString)
        }
    }

    override fun getDefaultQuoteStyle(): QuoteStyle {
        return when (sharedPreferences.getString(QUOTE_STYLE_KEY, "DefaultTheme")) {
            "CodeSnippetTheme" -> QuoteStyle.CODE_SNIPPET_THEME
//            "SpotifyTheme" -> QuoteStyle.SpotifyTheme
            "bratTheme" -> QuoteStyle.BRAT_THEME
            "igorTheme" -> QuoteStyle.IGOR_THEME
            "LiquidGlassTheme" -> QuoteStyle.LIQUID_GLASS_THEME
            "ReminderTheme" -> QuoteStyle.REMINDER_THEME
            else -> QuoteStyle.DEFAULT_THEME
        }
    }

    companion object {
        private const val QUOTE_STYLE_KEY = "quote_style_key"
    }
}