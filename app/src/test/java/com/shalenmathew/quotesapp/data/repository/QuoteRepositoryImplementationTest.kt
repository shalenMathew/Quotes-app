package com.shalenmathew.quotesapp.data.repository

import com.shalenmathew.quotesapp.data.local.QuoteDao
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.remote.QuoteApi
import com.shalenmathew.quotesapp.domain.model.Quote
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
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

    @Test
    fun ` throwExceptionMessage returns correct string for every error branch `(){

        fun mockHttpException(code: Int): HttpException {
            val response = Response.error<Any>(code, "".toResponseBody())
            return HttpException(response)
        }

        val testScenarios = mapOf(
            IOException() to "Network error. Please check your connection.",
            mockHttpException(401) to "Unauthorized Request",
            mockHttpException(429) to "Too many requests to the server, please check back in some time",
            mockHttpException(500) to "Server is down...Please try again later",
            mockHttpException(404) to "Unknown error, please try again.",
            IllegalArgumentException() to "Something went wrong. Please try again."
        )

        testScenarios.forEach { exception, expectedMessage ->

            val result = repository.throwExceptionMessage(exception)

            assertEquals(
                message = "Failed for ${exception::class.java.simpleName} with code ${if (exception is HttpException) exception.code() else "N/A"}",
                expected =  expectedMessage,
                actual = result
            )

        }

    }
}