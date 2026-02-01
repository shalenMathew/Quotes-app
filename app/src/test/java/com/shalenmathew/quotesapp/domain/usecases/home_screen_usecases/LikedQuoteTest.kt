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
class LikedQuoteTest {

    @Mock
    private lateinit var quoteRepository: QuoteRepository

    private lateinit var likedQuote: LikedQuote

    @Before
    fun setUp() {
        likedQuote = LikedQuote(quoteRepository)
    }

    @Test
    fun `should toggle liked to true and save when quote is not liked`() = runTest {
        val quote = Quote(1, "Test quote", "Author", false)

        val result = likedQuote(quote)

        assertEquals(true, result.liked)
        assertEquals(quote.id, result.id)
        verify(quoteRepository).saveLikedQuote(result)
    }

    @Test
    fun `should toggle liked to false and save when quote is already liked`() = runTest {
        val quote = Quote(1, "Test quote", "Author", true)

        val result = likedQuote(quote)

        assertEquals(false, result.liked)
        assertEquals(quote.id, result.id)
        verify(quoteRepository).saveLikedQuote(result)
    }

    @Test
    fun `should fetch quote by id and toggle liked`() = runTest {
        val existingQuote = Quote(1, "Test quote", "Author", false)
        whenever(quoteRepository.getQuoteById(1)).thenReturn(existingQuote)

        val result = likedQuote(1)

        assertEquals(true, result!!.liked)
        verify(quoteRepository).getQuoteById(1)
        verify(quoteRepository).saveLikedQuote(result)
    }

    @Test
    fun `should return null when quote id does not exist`() = runTest {
        whenever(quoteRepository.getQuoteById(999)).thenReturn(null)

        val result = likedQuote(999)

        assertNull(result)
        verify(quoteRepository).getQuoteById(999)
    }
}