package com.example.quotesapp.domain.usecases.fav_screen_usecases

import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.domain.model.QuoteHome
import com.example.quotesapp.domain.repository.FavQuoteRepository
import com.example.quotesapp.domain.repository.QuoteRepository
import com.example.quotesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavQuote @Inject constructor(private val quoteRepository: FavQuoteRepository) {

    operator fun invoke(): Flow<Resource<List<Quote>>> {
        return quoteRepository.getAllLikedQuotes()
    }

}