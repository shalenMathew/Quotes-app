package com.shalenmathew.quotesapp.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.shalenmathew.quotesapp.data.local.QuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.Quote
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
    fun test_insertQuotes() = runBlocking{

        val quote= Quote(1,"quote","author",false)
        quoteDao.insertQuoteList(listOf(quote))

        val result = quoteDao.getAllQuotes()

        assertEquals("quote",result[0].quote)

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