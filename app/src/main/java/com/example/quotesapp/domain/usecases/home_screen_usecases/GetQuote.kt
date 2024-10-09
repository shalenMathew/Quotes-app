package com.example.quotesapp.domain.usecases.home_screen_usecases

import com.example.quotesapp.domain.model.QuoteHome
import com.example.quotesapp.domain.repository.QuoteRepository
import com.example.quotesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuote @Inject constructor(private val quoteRepository: QuoteRepository) {
    operator fun invoke(): Flow<Resource<QuoteHome>> {
        return quoteRepository.getQuote()
    }
}