package com.shalenmathew.quotesapp.data.repository

import androidx.room.withTransaction
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.CollectionQuoteCrossRef
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

    override fun getCustomQuotesCount(): Flow<Int> {
        return db.getCustomQuoteDao().getCustomQuotesCount()
    }

    override suspend fun saveCustomQuote(quote: CustomQuote) {
        db.getCustomQuoteDao().insertCustomQuote(quote)
    }

    override suspend fun saveCustomQuotesBatch(quotes: List<CustomQuote>, collectionId: Int?) {
        if (quotes.isEmpty()) return
        db.withTransaction {
            val insertedIds = db.getCustomQuoteDao().insertCustomQuotes(quotes)
            if (collectionId != null && collectionId > 0) {
                val crossRefs = insertedIds.map { id ->
                    CollectionQuoteCrossRef(
                        collectionId = collectionId,
                        quoteId = id.toInt(),
                        isCustom = true
                    )
                }
                db.getCollectionDao().insertQuotesToCollection(crossRefs)
            }
        }
    }

    override suspend fun deleteCustomQuote(quote: CustomQuote) {
        db.withTransaction {
            db.getCustomQuoteDao().deleteCustomQuote(quote)
            db.getCollectionDao().deleteCrossRefsForQuote(quote.id, true)
        }
    }

    override suspend fun deleteCustomQuotesBatch(quotes: List<CustomQuote>) {
        if (quotes.isEmpty()) return
        db.withTransaction {
            db.getCustomQuoteDao().deleteCustomQuotes(quotes)
            quotes.forEach { quote ->
                db.getCollectionDao().deleteCrossRefsForQuote(quote.id, true)
            }
        }
    }

    override suspend fun updateCustomQuote(quote: CustomQuote) {
        db.getCustomQuoteDao().updateCustomQuote(quote)
    }
}