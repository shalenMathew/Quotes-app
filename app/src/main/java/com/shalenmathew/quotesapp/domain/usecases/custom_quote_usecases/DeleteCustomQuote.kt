package com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases

import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.repository.CustomQuoteRepository
import javax.inject.Inject

class DeleteCustomQuote @Inject constructor(
    private val repository: CustomQuoteRepository
) {
    suspend operator fun invoke(quote: CustomQuote) {
        repository.deleteCustomQuote(quote)
    }
}