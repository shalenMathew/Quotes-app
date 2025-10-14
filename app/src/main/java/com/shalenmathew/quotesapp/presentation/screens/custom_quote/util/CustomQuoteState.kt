package com.shalenmathew.quotesapp.presentation.screens.custom_quote.util

import com.shalenmathew.quotesapp.domain.model.CustomQuote

data class CustomQuoteState(
    val customQuotes: List<CustomQuote> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)