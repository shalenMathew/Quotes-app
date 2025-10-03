package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

data class QuoteUseCase(
    val getQuote: GetQuote,
    val likedQuote: LikedQuote,
    val getLikedQuotes: GetLikedQuotes
)
