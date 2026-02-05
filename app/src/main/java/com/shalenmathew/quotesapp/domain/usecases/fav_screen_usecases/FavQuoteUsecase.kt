package com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases

data class FavQuoteUseCase(
    val getFavQuote: GetFavQuote,
    val favLikedQuote: FavLikedQuote
)