package com.shalenmathew.quotesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.model.CollectionQuoteCrossRef
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: Collection): Long

    @Update
    suspend fun updateCollection(collection: Collection)

    @Delete
    suspend fun deleteCollection(collection: Collection)

    @Query("SELECT * FROM collections ORDER BY createdAt DESC, id DESC")
    fun getAllCollections(): Flow<List<Collection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuoteToCollection(crossRef: CollectionQuoteCrossRef)

    @Delete
    suspend fun deleteQuoteFromCollection(crossRef: CollectionQuoteCrossRef)

    @Query("SELECT * FROM collection_quote_cross_ref WHERE collectionId = :collectionId")
    fun getCrossRefsForCollection(collectionId: Int): Flow<List<CollectionQuoteCrossRef>>

    @Query("""
        SELECT q.* FROM Quote q
        INNER JOIN collection_quote_cross_ref ref ON q.id = ref.quoteId
        WHERE ref.collectionId = :collectionId AND ref.isCustom = 0
        AND (LOWER(q.quote) LIKE '%' || LOWER(:query) || '%' OR LOWER(q.author) LIKE '%' || LOWER(:query) || '%')
        ORDER BY q.updatedAt DESC, q.id DESC
    """)
    fun searchQuotesInCollection(collectionId: Int, query: String): Flow<List<Quote>>

    @Query("""
        SELECT cq.* FROM custom_quotes cq
        INNER JOIN collection_quote_cross_ref ref ON cq.id = ref.quoteId
        WHERE ref.collectionId = :collectionId AND ref.isCustom = 1
        AND (LOWER(cq.quote) LIKE '%' || LOWER(:query) || '%' OR LOWER(cq.author) LIKE '%' || LOWER(:query) || '%')
        ORDER BY cq.createdAt DESC, cq.id DESC
    """)
    fun searchCustomQuotesInCollection(collectionId: Int, query: String): Flow<List<CustomQuote>>

    @Query("SELECT * FROM collections WHERE id = :id")
    suspend fun getCollectionById(id: Int): Collection?

    @Query("SELECT * FROM collections WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getCollectionByName(name: String): Collection?

    @Query("SELECT EXISTS(SELECT 1 FROM collection_quote_cross_ref WHERE collectionId = :collectionId AND quoteId = :quoteId AND isCustom = :isCustom)")
    suspend fun isQuoteInCollection(collectionId: Int, quoteId: Int, isCustom: Boolean): Boolean

    @Query("SELECT collectionId FROM collection_quote_cross_ref WHERE quoteId = :quoteId AND isCustom = :isCustom")
    fun getCollectionIdsForQuote(quoteId: Int, isCustom: Boolean): Flow<List<Int>>

    @Query("DELETE FROM collection_quote_cross_ref WHERE quoteId = :quoteId AND isCustom = :isCustom")
    suspend fun deleteCrossRefsForQuote(quoteId: Int, isCustom: Boolean)

    @Query("DELETE FROM collection_quote_cross_ref WHERE isCustom = 0")
    suspend fun deleteAllSystemQuoteCrossRefs()

    @Query("SELECT * FROM collection_quote_cross_ref")
    suspend fun getAllCrossRefs(): List<CollectionQuoteCrossRef>
}
