package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface FavQuoteRepository {

    fun getAllLikedQuotes(): Flow<List<Quote>>

    suspend  fun saveLikedQuote(quote: Quote)
}