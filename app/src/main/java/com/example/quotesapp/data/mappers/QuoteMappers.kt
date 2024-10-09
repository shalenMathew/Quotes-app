package com.example.quotesapp.data.mappers

import com.example.quotesapp.data.remote.dto.QuotesDto
import com.example.quotesapp.data.remote.dto.QuotesDtoItem
import com.example.quotesapp.domain.model.Quote


fun QuotesDtoItem.toQuote():Quote{
    return  Quote(quote =q, author =a, liked = false)

}