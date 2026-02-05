package com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.FavQuoteRepository
import javax.inject.Inject

class FavLikedQuote @Inject constructor(val quoteRepository: FavQuoteRepository) {

    suspend operator fun invoke(quote: Quote): Quote {

        val updatedQuote = quote.copy(liked = !quote.liked)
        quoteRepository.saveLikedQuote(updatedQuote)
        return updatedQuote
    }
}