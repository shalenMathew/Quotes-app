package com.shalenmathew.quotesapp.data.remote

import coil.network.HttpException
import com.google.gson.GsonBuilder
import com.shalenmathew.quotesapp.data.remote.dto.QuotesDto
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.Test
import kotlin.test.assertEquals

class QuoteApiTest {

    private lateinit var server: MockWebServer
    private lateinit var quoteApi: QuoteApi

    @Before
    fun setUp() {
        server = MockWebServer()
        quoteApi = Retrofit.Builder()
                   .baseUrl(server.url("/"))
                   .addConverterFactory(GsonConverterFactory.create())
                   .build()
                   .create(QuoteApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }


    @Test
    fun quotesApi_success() = runTest{

        val quotesDto = QuotesDto()

        val gson = GsonBuilder().create()
        val json = gson.toJson(quotesDto)
        val res = MockResponse()
        res.setBody(json)
        server.enqueue(res)

        val result = quoteApi.getQuotesList()
        server.takeRequest()

        assertEquals(expected = quotesDto , actual = result)


    }


    @Test
    fun quotesApi_fail() = runTest{

        val res = MockResponse()
        res.setResponseCode(400)
        res.setBody("something went wrong")
        server.enqueue(res)


        try {
            val result = quoteApi.getQuotesList()
        }catch (e: retrofit2.HttpException){
            assertEquals(expected = 400, actual = e.code())
        }


    }


}