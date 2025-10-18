package com.shalenmathew.quotesapp.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.shalenmathew.quotesapp.data.local.QuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.repository.FavQuoteRepositoryImpl
import com.shalenmathew.quotesapp.domain.model.Quote
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavQuoteRepositoryImplTest {

    lateinit var quoteDatabase: QuoteDatabase
    lateinit var quoteDao: QuoteDao
    lateinit var favQuoteRepoImpl : FavQuoteRepositoryImpl

    @Before
    fun setUp(){
        quoteDatabase = Room
            .inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), QuoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        quoteDao = quoteDatabase.getQuoteDao()
        favQuoteRepoImpl = FavQuoteRepositoryImpl(quoteDatabase)


    }

    @After
    fun teardown() {
        quoteDatabase.close()
    }


    @Test
    fun test_searchQuotesWithoutQuery() = runBlocking {

        /// CAUTION :  liked should be true for the test to pass or it will fail even when test logic is correct

        val quote = Quote(0, "life is good", "future", liked = true, updatedAt = 100L)
        val quote2 = Quote(1, "good is life", "past", liked = true, updatedAt = 200L)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = favQuoteRepoImpl.getAllLikedQuotes("").first()

        assertEquals("good is life",result[0].quote)

    }


    @Test
    fun test_searchQuotesWithQuery() = runBlocking {

        val quote= Quote(0,"life is good","future",true)
        val quote2= Quote(1,"good is life","past",true)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = favQuoteRepoImpl.getAllLikedQuotes("good is life").first()

        assertEquals("good is life",result[0].quote)

    }


    @Test
    fun test_getAllLikedQuotes_returnsEmpty_whenNoQuotesAreLiked() = runBlocking {

        val quote= Quote(0,"life is good","future",false)
        val quote2= Quote(1,"good is life","past",false)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = favQuoteRepoImpl.getAllLikedQuotes("").first()

        assertEquals(0,result.size)

    }


}