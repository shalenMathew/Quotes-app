package com.example.quotesapp.presentation.home_screen.util

import com.example.quotesapp.domain.model.Quote

//enum class QuoteEvent {
//    LIKE,
//    SHARE
//}
// replacing this with sealed class for more customization

sealed class QuoteEvent {
    data class Like(val quote: Quote):QuoteEvent()
    data object Retry: QuoteEvent()
}