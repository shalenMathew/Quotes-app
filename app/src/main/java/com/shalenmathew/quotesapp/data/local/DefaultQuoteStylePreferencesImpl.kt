package com.shalenmathew.quotesapp.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle
import com.shalenmathew.quotesapp.util.Constants
import jakarta.inject.Inject

class DefaultQuoteStylePreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : DefaultQuoteStylePreferences {

    override fun saveDefaultQuoteStyle(quoteStyle: QuoteStyle) {
        val quoteStyleString = when (quoteStyle) {
            is QuoteStyle.DefaultTheme -> Constants.DEFAULT_THEME
            is QuoteStyle.CodeSnippetTheme -> Constants.CODE_SNIPPET_THEME
            is QuoteStyle.DiceDreamsTheme -> Constants.DICE_DREAMS_THEME
            is QuoteStyle.LiquidGlassTheme -> Constants.LIQUID_GLASS_THEME
            is QuoteStyle.BratTheme -> Constants.BRAT_THEME
            is QuoteStyle.IgorTheme -> Constants.IGOR_THEME
            is QuoteStyle.ReminderTheme -> Constants.REMINDER_THEME
            is QuoteStyle.TravelCardTheme -> Constants.TRAVEL_CARD_THEME
            is QuoteStyle.FlippingGoesTheme -> Constants.FLIPPING_GOES_THEME
            is QuoteStyle.MinimalBlackTheme -> Constants.MINIMAL_BLACK_THEME
            is QuoteStyle.MinimalBrownTheme -> Constants.MINIMAL_BROWN_THEME
            is QuoteStyle.YoutubeTheme -> Constants.YOUTUBE_THEME
            is QuoteStyle.ArtisanCardTheme -> Constants.ARTISAN_CARD_THEME
            is QuoteStyle.BookLookTheme -> Constants.BOOK_LOOK_THEME
        }
        sharedPreferences.edit {
            putString(QUOTE_STYLE_KEY, quoteStyleString)
        }
    }

    override fun getDefaultQuoteStyle(): QuoteStyle {
        return when (sharedPreferences.getString(QUOTE_STYLE_KEY, Constants.DEFAULT_THEME)) {
            Constants.CODE_SNIPPET_THEME -> QuoteStyle.CodeSnippetTheme
            Constants.DICE_DREAMS_THEME -> QuoteStyle.DiceDreamsTheme
            Constants.BRAT_THEME -> QuoteStyle.BratTheme
            Constants.IGOR_THEME -> QuoteStyle.IgorTheme
            Constants.LIQUID_GLASS_THEME -> QuoteStyle.LiquidGlassTheme
            Constants.REMINDER_THEME -> QuoteStyle.ReminderTheme
            Constants.TRAVEL_CARD_THEME -> QuoteStyle.TravelCardTheme
            Constants.FLIPPING_GOES_THEME -> QuoteStyle.FlippingGoesTheme
            Constants.MINIMAL_BLACK_THEME -> QuoteStyle.MinimalBlackTheme
            Constants.MINIMAL_BROWN_THEME -> QuoteStyle.MinimalBrownTheme
            Constants.YOUTUBE_THEME -> QuoteStyle.YoutubeTheme
            Constants.ARTISAN_CARD_THEME -> QuoteStyle.ArtisanCardTheme
            Constants.BOOK_LOOK_THEME -> QuoteStyle.BookLookTheme
            else -> QuoteStyle.DefaultTheme
        }
    }

    companion object {
        private const val QUOTE_STYLE_KEY = "quote_style_key"
    }
}