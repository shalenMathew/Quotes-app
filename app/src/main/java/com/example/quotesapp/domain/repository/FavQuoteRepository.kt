package com.example.quotesapp.domain.repository

import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface FavQuoteRepository {

    fun getAllLikedQuotes(): Flow<Resource<List<Quote>>>

    suspend  fun saveLikedQuote(quote: Quote)
}