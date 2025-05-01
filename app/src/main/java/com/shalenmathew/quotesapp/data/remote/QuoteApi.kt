package com.shalenmathew.quotesapp.data.remote

import com.shalenmathew.quotesapp.data.remote.dto.QuotesDto
import retrofit2.http.GET

interface QuoteApi {

    @GET("quotes")
   suspend fun getQuotesList():QuotesDto

    @GET("today")
   suspend fun getQuoteOfTheDay():QuotesDto

}