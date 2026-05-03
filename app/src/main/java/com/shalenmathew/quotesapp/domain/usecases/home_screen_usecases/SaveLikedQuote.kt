package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import javax.inject.Inject

class SaveLikedQuote @Inject constructor(
    private val repository: QuoteRepository
) {
    suspend operator fun invoke(quote: Quote) {
        repository.saveLikedQuote(quote)
    }
}
