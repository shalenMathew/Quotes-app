package com.shalenmathew.quotesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.presentation.screens.share_screen.QuoteStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareQuoteViewModel @Inject constructor(
    private val defaultQuoteStylePreferences: DefaultQuoteStylePreferences
) : ViewModel() {

    fun getDefaultQuoteStyle(): QuoteStyle {
        return defaultQuoteStylePreferences.getDefaultQuoteStyle()
    }

    fun changeDefaultQuoteStyle(quoteStyle: QuoteStyle) {
        defaultQuoteStylePreferences.saveDefaultQuoteStyle(quoteStyle)
    }
}