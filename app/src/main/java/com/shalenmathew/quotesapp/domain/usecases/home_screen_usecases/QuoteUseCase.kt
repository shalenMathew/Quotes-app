package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

import javax.inject.Inject

class QuoteUseCase @Inject constructor(
    val getQuote: GetQuote,
    val likedQuote: LikedQuote,
    val getLikedQuotes: GetLikedQuotes
) {
    // This is a composite use case that combines multiple use cases
    // Individual use cases can be accessed through the properties above
}
