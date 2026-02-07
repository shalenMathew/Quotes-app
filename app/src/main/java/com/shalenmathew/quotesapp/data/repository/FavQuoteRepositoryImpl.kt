package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.FavQuoteRepository
import kotlinx.coroutines.flow.Flow

class FavQuoteRepositoryImpl(private val db: QuoteDatabase) : FavQuoteRepository {

    override fun getAllLikedQuotes(query: String): Flow<List<Quote>> {

        return if (query.isNotBlank()) {
            db.getQuoteDao().searchForQuotes(query)

        } else {
            db.getQuoteDao().getAllLikedQuotes()
        }
    }

    override suspend fun saveLikedQuote(quote: Quote) {
        // this fun only saves liked quotes that's its job
        db.getQuoteDao().insertLikedQuote(quote)
    }
}