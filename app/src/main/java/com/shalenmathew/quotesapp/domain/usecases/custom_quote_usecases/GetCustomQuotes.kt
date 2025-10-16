package com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases

import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.repository.CustomQuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomQuotes @Inject constructor(
    private val repository: CustomQuoteRepository
) {
    operator fun invoke(query: String): Flow<List<CustomQuote>> {
        return repository.getAllCustomQuotes(query)
    }
}