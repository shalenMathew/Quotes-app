package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.QuoteHome
import com.shalenmathew.quotesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {

    fun getQuote(): Flow<Resource<QuoteHome>>

    suspend fun saveLikedQuote(quote: Quote)

    fun getAllLikedQuotes(): Flow<List<Quote>>

}