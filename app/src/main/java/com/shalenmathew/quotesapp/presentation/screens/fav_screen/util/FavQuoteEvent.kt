package com.shalenmathew.quotesapp.presentation.screens.fav_screen.util

import com.shalenmathew.quotesapp.domain.model.Quote

//enum class QuoteEvent {
//    LIKE,
//    SHARE
//}
// replacing this with sealed class for more customization

sealed class FavQuoteEvent {
    data class Like(val quote: Quote):FavQuoteEvent()
    data class onSearchQueryChanged(val query: String): FavQuoteEvent()
    object onRefresh: FavQuoteEvent()
}