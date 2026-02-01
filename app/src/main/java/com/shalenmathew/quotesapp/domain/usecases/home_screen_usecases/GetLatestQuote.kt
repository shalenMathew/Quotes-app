package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import javax.inject.Inject

class GetLatestQuote @Inject constructor(private val quoteRepository: QuoteRepository) {
    suspend operator fun invoke(): Quote? {
        return quoteRepository.getLatestQuote()
    }
}