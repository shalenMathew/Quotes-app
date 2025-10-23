package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.repository.CustomQuoteRepository
import kotlinx.coroutines.flow.Flow

class CustomQuoteRepositoryImpl(private val db: QuoteDatabase) : CustomQuoteRepository {

    override fun getAllCustomQuotes(query: String): Flow<List<CustomQuote>> {
        return if (query.isNotBlank()) {
            db.getCustomQuoteDao().searchCustomQuotes(query)
        } else {
            db.getCustomQuoteDao().getAllCustomQuotes()
        }
    }

    override suspend fun saveCustomQuote(quote: CustomQuote) {
        db.getCustomQuoteDao().insertCustomQuote(quote)
    }

    override suspend fun deleteCustomQuote(quote: CustomQuote) {
        db.getCustomQuoteDao().deleteCustomQuote(quote)
    }
}