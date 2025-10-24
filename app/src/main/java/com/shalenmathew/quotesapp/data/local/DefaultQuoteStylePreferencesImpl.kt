package com.shalenmathew.quotesapp.data.local

import android.content.SharedPreferences
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle

class DefaultQuoteStylePreferencesImpl(
    private val sharedPreferences: SharedPreferences
) : DefaultQuoteStylePreferences {

    companion object {
        private const val KEY_DEFAULT_STYLE = "default_quote_style"
    }

    override fun saveDefaultQuoteStyle(quoteStyle: QuoteStyle) {
        sharedPreferences.edit()
            .putString(KEY_DEFAULT_STYLE, quoteStyle::class.simpleName)
            .apply()
    }

    override fun getDefaultQuoteStyle(): QuoteStyle {
        val saved = sharedPreferences.getString(KEY_DEFAULT_STYLE, QuoteStyle.DefaultTheme::class.simpleName)

        return when (saved) {
            QuoteStyle.DefaultTheme::class.simpleName -> QuoteStyle.DefaultTheme
            QuoteStyle.CodeSnippetTheme::class.simpleName -> QuoteStyle.CodeSnippetTheme
            QuoteStyle.LiquidGlassTheme::class.simpleName -> QuoteStyle.LiquidGlassTheme
            QuoteStyle.bratTheme::class.simpleName -> QuoteStyle.bratTheme
            QuoteStyle.igorTheme::class.simpleName -> QuoteStyle.igorTheme
            QuoteStyle.ReminderTheme::class.simpleName -> QuoteStyle.ReminderTheme
            QuoteStyle.FliplingoesTheme::class.simpleName -> QuoteStyle.FliplingoesTheme
            else -> QuoteStyle.DefaultTheme
        }
    }
}
