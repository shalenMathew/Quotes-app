package com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases

import javax.inject.Inject

class FavQuoteUseCase @Inject constructor(
    val getFavQuote: GetFavQuote,
    val favLikedQuote: FavLikedQuote
) {
    // This is a composite use case that combines multiple use cases
    // Individual use cases can be accessed through the properties above
}