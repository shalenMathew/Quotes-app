package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle

interface DefaultQuoteStylePreferences {
    fun saveDefaultQuoteStyle(quoteStyle: QuoteStyle)
    fun getDefaultQuoteStyle(): QuoteStyle
}