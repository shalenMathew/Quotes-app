package com.example.quotesapp.domain.usecases.fav_screen_usecases

import android.util.Log
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.domain.repository.FavQuoteRepository
import javax.inject.Inject

class FavLikedQuote @Inject constructor(val quoteRepository: FavQuoteRepository) {

    suspend operator fun  invoke(quote:Quote): Quote {

        val updatedQuote = quote.copy(liked = !quote.liked)
        quoteRepository.saveLikedQuote(updatedQuote)
        Log.d("TAG","use case -"+updatedQuote.liked.toString())

        return updatedQuote
    }
}