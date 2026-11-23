package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.QuoteHome
import com.shalenmathew.quotesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface QuoteRepository {

    fun getQuote(): Flow<Resource<QuoteHome>>

    suspend fun saveLikedQuote(quote: Quote)
    suspend fun saveLikedQuotesBatch(quotes: List<Quote>) {
        quotes.forEach { saveLikedQuote(it) }
    }

    fun getAllLikedQuotes(): Flow<List<Quote>>
    fun getLikedQuotesCount(): Flow<Int> = getAllLikedQuotes().map { it.size }

    suspend fun getQuoteById(id: Int): Quote?

    suspend fun getLatestQuote(): Quote?

    suspend fun markAsDisplayed(quoteId: Int)
    suspend fun getUndisplayedCount(): Int
    suspend fun getUndisplayedQuotes(): Resource<List<Quote>>

    suspend fun fetchRandomRemoteQuote(): Resource<Quote>

}