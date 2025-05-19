package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.QuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.mappers.toQuote
import com.shalenmathew.quotesapp.data.remote.QuoteApi
import com.shalenmathew.quotesapp.data.remote.dto.QuotesDto
import com.shalenmathew.quotesapp.data.remote.dto.QuotesDtoItem
import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class QuoteRepositoryImplementationTest {

    private lateinit var api: QuoteApi
    private lateinit var db: QuoteDatabase
    private lateinit var dao:QuoteDao
    private lateinit var repository: QuoteRepositoryImplementation

    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
       Dispatchers.setMain(testDispatcher)
        api = mock()
        db = mock()
        dao = mock()

        repository = QuoteRepositoryImplementation(api, db)
        whenever(db.getQuoteDao()).thenReturn(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getQuote()= runTest(testDispatcher) {

        // arranging all desired results
        val quoteDto = arrayListOf<QuotesDtoItem>(QuotesDtoItem(a = "auth1", c = "", h = "", q = "quote1"),
            QuotesDtoItem(a = "auth2", c = "", h = "", q = "quote2")) as QuotesDto

       val quoteOfDay = listOf<QuotesDtoItem>(QuotesDtoItem(a = "qot", c = "", h = "", q = "qot"))

        val dbQuotes = quoteDto.map { it.toQuote() }

        whenever(api.getQuotesList()).thenReturn(quoteDto)



    }

}