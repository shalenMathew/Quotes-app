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

    @Query(" SELECT * FROM Quote WHERE liked==1 ORDER BY id ASC ")
    fun getAllLikedQuotes(): Flow<List<Quote> > // i think the list is not updated as no one is observing the data like flow or live data
    // list is static snapshot we need a observer like flow or live data

    // BUG FIXED - > the issue is i was not wrapping the list in any flow or live data causing into not observe the changes
    // and made the simple idea of fetching data from db complicated by using resources with it
    // resources are generally used with remote api than room

    @Query(" SELECT * FROM Quote ORDER BY id DESC ")
   suspend fun getAllQuotes():List<Quote>

    @Query("DELETE FROM quote")
    suspend fun deleteAll()

}