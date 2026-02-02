package com.shalenmathew.quotesapp.data.mappers

import com.shalenmathew.quotesapp.data.remote.dto.QuotesDtoItem
import com.shalenmathew.quotesapp.domain.model.Quote


fun QuotesDtoItem.toQuote(): Quote {
    return Quote(quote = q, author = a, liked = false)
}