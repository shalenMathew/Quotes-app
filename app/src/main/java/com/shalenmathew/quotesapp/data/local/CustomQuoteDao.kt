package com.shalenmathew.quotesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomQuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomQuote(quote: CustomQuote)

    @Delete
    suspend fun deleteCustomQuote(quote: CustomQuote)

    @Query("SELECT * FROM custom_quotes ORDER BY createdAt DESC, id DESC")
    fun getAllCustomQuotes(): Flow<List<CustomQuote>>

    @Query(
        """
        SELECT * FROM custom_quotes 
        WHERE LOWER(quote) LIKE '%' || LOWER(:query) || '%'
        OR LOWER(author) LIKE '%' || LOWER(:query) || '%'
        ORDER BY createdAt DESC, id DESC
    """
    )
    fun searchCustomQuotes(query: String): Flow<List<CustomQuote>>

    @Update
    suspend fun updateCustomQuote(quote: CustomQuote)

}