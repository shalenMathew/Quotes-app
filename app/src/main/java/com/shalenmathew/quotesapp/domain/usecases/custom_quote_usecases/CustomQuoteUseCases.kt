package com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases

data class CustomQuoteUseCases(
    val getCustomQuotes: GetCustomQuotes,
    val saveCustomQuote: SaveCustomQuote,
    val deleteCustomQuote: DeleteCustomQuote
)