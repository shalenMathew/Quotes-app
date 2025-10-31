package com.shalenmathew.quotesapp.data.local

import android.content.SharedPreferences
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle
import androidx.core.content.edit
import com.shalenmathew.quotesapp.util.Constants
import jakarta.inject.Inject

class DefaultQuoteStylePreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): DefaultQuoteStylePreferences {

    override fun saveDefaultQuoteStyle(quoteStyle: QuoteStyle) {
        val quoteStyleString = when (quoteStyle) {
            is QuoteStyle.DefaultTheme -> Constants.DefaultTheme
            is QuoteStyle.CodeSnippetTheme -> Constants.CodeSnippetTheme
            is QuoteStyle.LiquidGlassTheme -> Constants.LiquidGlassTheme
            is QuoteStyle.bratTheme -> Constants.bratTheme
            is QuoteStyle.igorTheme -> Constants.igorTheme
            is QuoteStyle.ReminderTheme -> Constants.ReminderTheme
            is QuoteStyle.TravelCardTheme -> Constants.TravelTheme
            is QuoteStyle.FliplingoesTheme -> Constants.FliplingoesTheme
            is QuoteStyle.MinimalBlackTheme -> Constants.MinimalBlackTheme
            is QuoteStyle.MinimalBrownTheme -> Constants.MinimalBrownTheme
        }
        sharedPreferences.edit {
            putString(QUOTE_STYLE_KEY, quoteStyleString)
        }
    }

    override fun getDefaultQuoteStyle(): QuoteStyle {
        return when (sharedPreferences.getString(QUOTE_STYLE_KEY, Constants.DefaultTheme)) {
            Constants.CodeSnippetTheme -> QuoteStyle.CodeSnippetTheme
            Constants.bratTheme -> QuoteStyle.bratTheme
            Constants.igorTheme -> QuoteStyle.igorTheme
            Constants.LiquidGlassTheme -> QuoteStyle.LiquidGlassTheme
            Constants.ReminderTheme -> QuoteStyle.ReminderTheme
            Constants.TravelTheme -> QuoteStyle.TravelCardTheme
            Constants.FliplingoesTheme -> QuoteStyle.FliplingoesTheme
            Constants.MinimalBlackTheme -> QuoteStyle.MinimalBlackTheme
            Constants.MinimalBrownTheme -> QuoteStyle.MinimalBrownTheme
            else -> QuoteStyle.DefaultTheme
        }
    }

    companion object {
        private const val QUOTE_STYLE_KEY = "quote_style_key"
    }
}