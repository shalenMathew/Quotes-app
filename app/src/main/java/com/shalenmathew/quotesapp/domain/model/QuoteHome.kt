package com.shalenmathew.quotesapp.domain.model


data class QuoteHome (
val quotesList:List<Quote>,
 val quotesOfTheDay:List<Quote>
)