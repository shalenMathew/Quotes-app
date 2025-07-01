package com.shalenmathew.quotesapp.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.shalenmathew.quotesapp.data.local.QuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.Quote
import junit.framework.ComparisonFailure
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class QuotesDaoTest {

    lateinit var quoteDatabase: QuoteDatabase
    lateinit var quoteDao: QuoteDao

    @Before
    fun setUp(){
        quoteDatabase = Room
            .inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),QuoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        quoteDao = quoteDatabase.getQuoteDao()

    }


    @Test
    fun test_insertAndFetchQuotes() = runBlocking {

        val quote= Quote(1,"quote","author",false)
        quoteDao.insertQuoteList(listOf(quote))

        val result = quoteDao.getAllQuotes()

        assertEquals("quote",result[0].quote)

    }

    @Test
    fun test_searchQuotesWithoutQuery() = runBlocking {

        val quote= Quote(0,"life is good","future",true)

        val quote2= Quote(1,"good is life","past",true)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = quoteDao.searchForQuotes("").first() // if no query is passesd then the list will be in the order of quotes
        // inserted

        assertEquals("life is good",result[0].quote)

    }


    @Test
    fun test_searchQuotesForLikedQuotes() = runBlocking {

        val quote= Quote(0,"life is good","future",true)

        val quote2= Quote(1,"good is life","past",true)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = quoteDao.searchForQuotes("good is life").first()  // search only return list of liked quotes

        assertEquals("good is life",result[0].quote) /// the first index will have the most appropriate result

    }

    @Test
    fun test_searchQuotesForNotLikedQuotes() = runBlocking {

        val quote= Quote(0,"life is good","future",false)

        val quote2= Quote(1,"good is life","past",false)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = quoteDao.searchForQuotes("good is life").first()

        assertEquals(0,result.size)

    }


    @Test
    fun test_insertLikedQuote() = runBlocking{

        val quote= Quote(1,"quote","author",true)
        quoteDao.insertLikedQuote(quote)

        val result = quoteDao.getAllLikedQuotes().first()

        assertEquals(true,result[0].liked)
        assertEquals("author",result[0].author)

    }


    @Test
    fun test_delete() = runBlocking{

        val quote= Quote(1,"quote","author",true)
        quoteDao.insertLikedQuote(quote)

        val resultWhileInsert = quoteDao.getAllLikedQuotes().first()

        assertEquals(1,resultWhileInsert.size)

        quoteDao.deleteQuote(quote)

        val resultWhileDelete = quoteDao.getAllLikedQuotes().first()

        assertEquals(0,resultWhileDelete.size)


    }



    @After
    fun tearDown(){
        quoteDatabase.close()
    }

}