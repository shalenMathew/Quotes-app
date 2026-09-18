package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.model.CollectionQuoteCrossRef
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val db: QuoteDatabase
) : CollectionRepository {

    private val dao = db.getCollectionDao()

    override fun getAllCollections(): Flow<List<Collection>> = dao.getAllCollections()

    override suspend fun insertCollection(collection: Collection) = dao.insertCollection(collection)

    override suspend fun updateCollection(collection: Collection) = dao.updateCollection(collection)

    override suspend fun deleteCollection(collection: Collection) = dao.deleteCollection(collection)

    override suspend fun getCollectionById(id: Int): Collection? = dao.getCollectionById(id)

    override suspend fun addQuoteToCollection(collectionId: Int, quoteId: Int, isCustom: Boolean) {
        dao.insertQuoteToCollection(CollectionQuoteCrossRef(collectionId, quoteId, isCustom))
    }

    override suspend fun removeQuoteFromCollection(collectionId: Int, quoteId: Int, isCustom: Boolean) {
        dao.deleteQuoteFromCollection(CollectionQuoteCrossRef(collectionId, quoteId, isCustom))
    }

    override suspend fun isQuoteInCollection(collectionId: Int, quoteId: Int, isCustom: Boolean): Boolean {
        return dao.isQuoteInCollection(collectionId, quoteId, isCustom)
    }

    override fun getCollectionIdsForQuote(quoteId: Int, isCustom: Boolean): Flow<List<Int>> {
        return dao.getCollectionIdsForQuote(quoteId, isCustom)
    }

    override fun searchQuotesInCollection(collectionId: Int, query: String): Flow<List<Quote>> {
        return dao.searchQuotesInCollection(collectionId, query)
    }

    override fun searchCustomQuotesInCollection(collectionId: Int, query: String): Flow<List<CustomQuote>> {
        return dao.searchCustomQuotesInCollection(collectionId, query)
    }
}
