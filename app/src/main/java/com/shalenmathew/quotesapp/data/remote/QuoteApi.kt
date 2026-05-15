package com.shalenmathew.quotesapp.data.remote

import com.shalenmathew.quotesapp.data.remote.dto.QuotesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApi {

    @GET("quotes")
    suspend fun getQuotesList(@Query("t") timestamp: Long = System.currentTimeMillis()): QuotesDto

    @GET("today")
    suspend fun getQuoteOfTheDay(@Query("t") timestamp: Long = System.currentTimeMillis()): QuotesDto

    @GET("random")
    suspend fun getRandomQuote(@Query("t") timestamp: Long = System.currentTimeMillis()): QuotesDto

}