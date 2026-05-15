package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

data class QuoteUseCase(
    val getQuote: GetQuote,
    val likedQuote: LikedQuote,
    val saveLikedQuote: SaveLikedQuote,
    val getLikedQuotes: GetLikedQuotes,
    val getLatestQuote: GetLatestQuote,
    val markAsDisplayed: MarkAsDisplayed,
    val getUndisplayedQuotes: GetUndisplayedQuotes,
    val getRandomQuoteFromNetwork: GetRandomQuoteFromNetwork
)
