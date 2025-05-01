package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases


import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import javax.inject.Inject

class LikedQuote @Inject constructor(val quoteRepository: QuoteRepository) {

    suspend operator fun  invoke(quote:Quote):Quote{

        val updatedQuote = quote.copy(liked = !quote.liked)
        quoteRepository.saveLikedQuote(updatedQuote)

        return updatedQuote
    }
}