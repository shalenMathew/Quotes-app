package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import com.shalenmathew.quotesapp.util.Resource
import javax.inject.Inject

class GetUndisplayedQuotes @Inject constructor(private val quoteRepository: QuoteRepository) {
    suspend operator fun invoke(): Resource<List<Quote>> {
        return quoteRepository.getUndisplayedQuotes()
    }
}
