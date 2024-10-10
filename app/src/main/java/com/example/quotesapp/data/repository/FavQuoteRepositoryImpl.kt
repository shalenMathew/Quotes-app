package com.example.quotesapp.data.repository

import android.util.Log
import com.example.quotesapp.data.local.QuoteDatabase
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.domain.repository.FavQuoteRepository
import com.example.quotesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavQuoteRepositoryImpl (private val db: QuoteDatabase):FavQuoteRepository {

    override fun getAllLikedQuotes(): Flow<List<Quote>> {
    return db.getQuoteDao().getAllLikedQuotes()
    }

    override suspend fun saveLikedQuote(quote: Quote) {
        db.getQuoteDao().insertLikedQuote(quote)
    }
}