package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.QuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.remote.QuoteApi
import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(MockitoJUnitRunner::class)
class QuoteRepositoryImplementationTest {

    @Mock
    private lateinit var api: QuoteApi

    @Mock
    private lateinit var db: QuoteDatabase

    @Mock
    private lateinit var quoteDao: QuoteDao

    private lateinit var repository: QuoteRepositoryImplementation

    @Before
    fun setUp() {
        whenever(db.getQuoteDao()).thenReturn(quoteDao)
        repository = QuoteRepositoryImplementation(api, db)
    }

    @Test
    fun `should return quote when getQuoteById is called with valid id`() = runTest {
        val expectedQuote = Quote(1, "Test quote", "Test author", false)
        whenever(quoteDao.getQuoteById(1)).thenReturn(expectedQuote)

        val result = repository.getQuoteById(1)

        assertEquals(expectedQuote, result)
        verify(quoteDao).getQuoteById(1)
    }

    @Test
    fun `should return null when getQuoteById is called with non-existent id`() = runTest {
        whenever(quoteDao.getQuoteById(999)).thenReturn(null)

        val result = repository.getQuoteById(999)

        assertNull(result)
        verify(quoteDao).getQuoteById(999)
    }

    @Test
    fun `should return latest quote when getLatestQuote is called and quotes exist`() = runTest {
        val expectedQuote = Quote(5, "Latest quote", "Latest author", false)
        whenever(quoteDao.getLatestQuote()).thenReturn(expectedQuote)

        val result = repository.getLatestQuote()

        assertEquals(expectedQuote, result)
        verify(quoteDao).getLatestQuote()
    }

    @Test
    fun `should return null when getLatestQuote is called and no quotes exist`() = runTest {
        whenever(quoteDao.getLatestQuote()).thenReturn(null)

        val result = repository.getLatestQuote()

        assertNull(result)
        verify(quoteDao).getLatestQuote()
    }
}