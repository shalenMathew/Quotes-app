package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.FavQuoteRepository
import kotlinx.coroutines.flow.Flow

class FavQuoteRepositoryImpl (private val db: QuoteDatabase):FavQuoteRepository {

    override fun getAllLikedQuotes(): Flow<List<Quote>> {
    return db.getQuoteDao().getAllLikedQuotes()
    }

    override suspend fun saveLikedQuote(quote: Quote) {
        db.getQuoteDao().insertLikedQuote(quote)
    }
}