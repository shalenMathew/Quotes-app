package com.shalenmathew.quotesapp.domain.model

data class BackupData(
    val likedQuotes: List<Quote>,
    val customQuotes: List<CustomQuote>,
    val appVersionName: String
)
