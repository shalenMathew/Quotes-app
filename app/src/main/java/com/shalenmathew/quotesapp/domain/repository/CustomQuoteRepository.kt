package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.domain.model.CustomQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CustomQuoteRepository {
    fun getAllCustomQuotes(query: String): Flow<List<CustomQuote>>
    fun getCustomQuotesCount(): Flow<Int> = getAllCustomQuotes("").map { it.size }
    suspend fun saveCustomQuote(quote: CustomQuote)
    suspend fun saveCustomQuotesBatch(quotes: List<CustomQuote>, collectionId: Int? = null) {
        quotes.forEach { saveCustomQuote(it) }
    }
    suspend fun deleteCustomQuote(quote: CustomQuote)
    suspend fun deleteCustomQuotesBatch(quotes: List<CustomQuote>) {
        quotes.forEach { deleteCustomQuote(it) }
    }
    suspend fun updateCustomQuote(quote: CustomQuote)
}