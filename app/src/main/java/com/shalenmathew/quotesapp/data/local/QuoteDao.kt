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

    @Delete
    suspend fun deleteQuote(quote: Quote)

    @Query("DELETE FROM quote")
    suspend fun deleteAll()

    @Query(" SELECT * FROM Quote WHERE liked==1 ORDER BY updatedAt DESC ")
    fun getAllLikedQuotes(): Flow<List<Quote>> // i think the list was not being updated as there was nothing that was observing the data, like flow or live data
    // list is static snapshot we need a observer like flow or live data for our updates


    @Query(" SELECT * FROM Quote ORDER BY id DESC ")
    suspend fun getAllQuotes():List<Quote>
    // BUG FIXED - > the issue was i was not wrapping the list in any flow or live data causing it to not observe the changes
    // and made the simple idea of fetching data from db...
    // Resource<> are mostly used along with remote api than room


    @Query("""
  SELECT * FROM Quote
  WHERE liked == 1
    AND (
      LOWER(quote) LIKE '%' || LOWER(:query) || '%'
      OR LOWER(author) LIKE '%' || LOWER(:query) || '%'
    )
    ORDER BY updatedAt DESC
""")
    fun searchForQuotes(query:String): Flow<List<Quote> >

}