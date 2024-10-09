package com.example.quotesapp.domain.usecases.home_screen_usecases


import android.util.Log
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.domain.repository.QuoteRepository
import javax.inject.Inject

class LikedQuote @Inject constructor(val quoteRepository: QuoteRepository) {

    suspend operator fun  invoke(quote:Quote):Quote{

        val updatedQuote = quote.copy(liked = !quote.liked)
        quoteRepository.saveLikedQuote(updatedQuote)
        Log.d("TAG","use case -"+updatedQuote.liked.toString())

        return updatedQuote
    }
}