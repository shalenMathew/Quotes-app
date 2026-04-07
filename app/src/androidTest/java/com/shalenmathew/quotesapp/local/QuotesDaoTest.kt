package com.shalenmathew.quotesapp.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shalenmathew.quotesapp.data.local.QuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.Quote
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
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
    fun test_insertAndFetchQuotes() = runTest {

        val quote= Quote(1,"quote","author",false)
        quoteDao.insertQuoteList(listOf(quote))

        val result = quoteDao.getAllQuotes()

        Assert.assertEquals("quote",result[0].quote)
    }

    @Test
    fun test_searchInFavouritesWithoutQuery() = runTest {

        val quote= Quote(0,"life is good","future",true, updatedAt = 2000L)

        val quote2= Quote(1,"good is life","past",true, updatedAt = 1000L)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = quoteDao.searchForQuotes("").first() // if no query is passesd then the list will be in the order of quotes
        // inserted

        Assert.assertEquals(2,result.size)
        Assert.assertEquals("life is good",result[0].quote)

    }


    @Test
    fun test_searchForLikedQuotes() = runTest {

        val quote= Quote(0,"life is good","future",true)

        val quote2= Quote(1,"good is life","past",true)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = quoteDao.searchForQuotes("good is life").first()  // search only return list of liked quotes



        Assert.assertEquals("good is life",result[0].quote)  /// the first index will have the most appropriate result

    }

    @Test
    fun test_searchQuotesForNotLikedQuotes() = runTest {

        val quote= Quote(0,"life is good","future",false)

        val quote2= Quote(1,"good is life","past",false)

        quoteDao.insertQuoteList(listOf(quote,quote2))

        val result = quoteDao.searchForQuotes("good is life").first()


        Assert.assertEquals(0,result.size)

    }


    @Test
    fun test_insertLikedQuote() = runTest{

        val quote= Quote(1,"quote","author",true)
        quoteDao.insertLikedQuote(quote)

        val result = quoteDao.getAllLikedQuotes().first()

        Assert.assertEquals(true,result[0].liked)
        Assert.assertEquals("author",result[0].author)



    }


    @Test
    fun test_delete() = runTest{

        val quote= Quote(1,"quote","author",true)
        quoteDao.insertLikedQuote(quote)

        val resultWhileInsert = quoteDao.getAllLikedQuotes().first()

        assertEquals(1,resultWhileInsert.size)

        quoteDao.deleteQuote(quote)

        val resultWhileDelete = quoteDao.getAllLikedQuotes().first()

        Assert.assertEquals(0,resultWhileDelete.size)


    }

    @Test
    fun test_getQuoteById() = runTest {
        val quote = Quote(1, "quote", "author", false)
        quoteDao.insertQuoteList(listOf(quote))

        val result = quoteDao.getQuoteById(1)

        Assert.assertEquals("quote", result?.quote)
    }

    @Test
    fun test_displayStatusLogic() = runTest {

        val quote = Quote(1, "quote", "author", false, displayed = false)
        quoteDao.insertQuoteList(listOf(quote))

        assertEquals(1, quoteDao.getUndisplayedCount())

        quoteDao.markAsDisplayed(1)
        assertEquals(0, quoteDao.getUndisplayedCount())

        val likedQuote = Quote(2, "liked", "author", liked = true, displayed = true)
        val unlikedQuote = Quote(3, "unliked", "author", liked = false, displayed = true)
        quoteDao.insertQuoteList(listOf(likedQuote, unlikedQuote))

        quoteDao.resetDisplayedStatus()

        val undisplayed = quoteDao.getUndisplayedQuotes()
        assertEquals(1, undisplayed.size)
        assertEquals(3, undisplayed[0].id)
    }

    @After
    fun tearDown(){
        quoteDatabase.close()
    }

}