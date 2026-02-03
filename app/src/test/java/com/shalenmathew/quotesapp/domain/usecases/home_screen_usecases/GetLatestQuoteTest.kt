package com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases

import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
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
class GetLatestQuoteTest {

    @Mock
    private lateinit var quoteRepository: QuoteRepository

    private lateinit var getLatestQuote: GetLatestQuote

    @Before
    fun setUp() {
        getLatestQuote = GetLatestQuote(quoteRepository)
    }

    @Test
    fun `should return latest quote when quotes exist`() = runTest {
        val expectedQuote = Quote(5, "Latest quote", "Author", false)
        whenever(quoteRepository.getLatestQuote()).thenReturn(expectedQuote)

        val result = getLatestQuote()

        assertEquals(expectedQuote, result)
        verify(quoteRepository).getLatestQuote()
    }

    @Test
    fun `should return null when no quotes exist`() = runTest {
        whenever(quoteRepository.getLatestQuote()).thenReturn(null)

        val result = getLatestQuote()

        assertNull(result)
        verify(quoteRepository).getLatestQuote()
    }
}