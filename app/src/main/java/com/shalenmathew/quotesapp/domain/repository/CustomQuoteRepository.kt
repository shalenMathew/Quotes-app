package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.domain.model.CustomQuote
import kotlinx.coroutines.flow.Flow

interface CustomQuoteRepository {
    fun getAllCustomQuotes(query: String): Flow<List<CustomQuote>>
    suspend fun saveCustomQuote(quote: CustomQuote)
    suspend fun deleteCustomQuote(quote: CustomQuote)
}