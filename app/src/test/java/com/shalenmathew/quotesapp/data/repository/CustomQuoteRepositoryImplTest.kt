package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.CustomQuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CustomQuoteRepositoryImplTest {

    @Mock
    private lateinit var db: QuoteDatabase

    @Mock
    private lateinit var customQuoteDao: CustomQuoteDao

    private lateinit var repository: CustomQuoteRepositoryImpl

    @Before
    fun setUp() {
        whenever(db.getCustomQuoteDao()).thenReturn(customQuoteDao)
        repository = CustomQuoteRepositoryImpl(db)
    }

    @Test
    fun `getAllCustomQuotes with empty query returns all custom quotes`() = runTest {
        val quotes = listOf(CustomQuote(id = 1, quote = "Test 1", author = "Author 1"))
        whenever(customQuoteDao.getAllCustomQuotes()).thenReturn(flowOf(quotes))

        val result = repository.getAllCustomQuotes("").first()

        assertEquals(quotes, result)
    }

    @Test
    fun `getAllCustomQuotes with query returns filtered custom quotes`() = runTest {
        val query = "Test"
        val quotes = listOf(CustomQuote(id = 1, quote = "Test 1", author = "Author 1"))
        whenever(customQuoteDao.searchCustomQuotes(query)).thenReturn(flowOf(quotes))

        val result = repository.getAllCustomQuotes(query).first()

        assertEquals(quotes, result)
    }
}
