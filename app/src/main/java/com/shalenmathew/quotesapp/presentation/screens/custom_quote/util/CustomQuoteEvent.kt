package com.shalenmathew.quotesapp.presentation.screens.custom_quote.util

import com.shalenmathew.quotesapp.domain.model.CustomQuote

sealed class CustomQuoteEvent {
    data class SaveQuote(val quote: String, val author: String) : CustomQuoteEvent()
    data class DeleteQuote(val quote: CustomQuote) : CustomQuoteEvent()
    data class OnSearchQueryChanged(val query: String) : CustomQuoteEvent()
}