package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQuotes @Inject constructor(private val quoteRepository: QuoteRepository) {
    operator fun invoke(): Flow<List<Quote>> {
        return quoteRepository.getAllQuotes()
    }
}
