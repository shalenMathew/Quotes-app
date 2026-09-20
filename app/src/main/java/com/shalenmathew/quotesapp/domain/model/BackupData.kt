package com.shalenmathew.quotesapp.domain.model

data class BackupData(
    val likedQuotes: List<Quote>,
    val customQuotes: List<CustomQuote>,
    val collections: List<Collection> = emptyList(),
    val crossRefs: List<CollectionQuoteCrossRef> = emptyList(),
    val appVersionName: String
)
