package com.shalenmathew.quotesapp.domain.repository

import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.model.CollectionQuoteCrossRef
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getAllCollections(): Flow<List<Collection>>
    suspend fun insertCollection(collection: Collection)
    suspend fun updateCollection(collection: Collection)
    suspend fun deleteCollection(collection: Collection)
    suspend fun getCollectionById(id: Int): Collection?
    suspend fun getCollectionByName(name: String): Collection?

    suspend fun addQuoteToCollection(collectionId: Int, quoteId: Int, isCustom: Boolean)
    suspend fun removeQuoteFromCollection(collectionId: Int, quoteId: Int, isCustom: Boolean)
    suspend fun isQuoteInCollection(collectionId: Int, quoteId: Int, isCustom: Boolean): Boolean
    fun getCollectionIdsForQuote(quoteId: Int, isCustom: Boolean): Flow<List<Int>>
    
    fun searchQuotesInCollection(collectionId: Int, query: String): Flow<List<Quote>>
    fun searchCustomQuotesInCollection(collectionId: Int, query: String): Flow<List<CustomQuote>>
}
