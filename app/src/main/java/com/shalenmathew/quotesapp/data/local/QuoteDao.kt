package com.shalenmathew.quotesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow


@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuoteList(quote: List<Quote>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedQuote(quote: Quote)

    @Query("SELECT * FROM Quote WHERE id = :id LIMIT 1")
    suspend fun getQuoteById(id: Int): Quote?

    @Query("SELECT * FROM Quote ORDER BY id DESC LIMIT 1")
    suspend fun getLatestQuote(): Quote?

    @Delete
    suspend fun deleteQuote(quote: Quote)

    @Query("DELETE FROM quote")
    suspend fun deleteAll()

    @Query(" SELECT * FROM Quote WHERE liked==1 ORDER BY updatedAt DESC, id DESC ")
    fun getAllLikedQuotes(): Flow<List<Quote>>


    @Query(" SELECT * FROM Quote ORDER BY id DESC ")
    suspend fun getAllQuotes(): List<Quote>


    @Query(
        """
  SELECT * FROM Quote
  WHERE liked == 1
    AND (
      LOWER(quote) LIKE '%' || LOWER(:query) || '%'
      OR LOWER(author) LIKE '%' || LOWER(:query) || '%'
    )
    ORDER BY updatedAt DESC, id DESC
"""
    )
    fun searchForQuotes(query: String): Flow<List<Quote>>

    @Query("SELECT * FROM Quote WHERE displayed = 0 ORDER BY id DESC")
    suspend fun getUndisplayedQuotes(): List<Quote>

    @Query("SELECT COUNT(*) FROM Quote WHERE displayed = 0")
    suspend fun getUndisplayedCount(): Int

    @Query("UPDATE Quote SET displayed = 1 WHERE id = :quoteId")
    suspend fun markAsDisplayed(quoteId: Int)

    @Query("UPDATE Quote SET displayed = 0 WHERE liked = 0")
    suspend fun resetDisplayedStatus()

}