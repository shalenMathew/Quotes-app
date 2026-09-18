package com.shalenmathew.quotesapp.presentation.screens.library.util

import com.shalenmathew.quotesapp.domain.model.Quote

data class CollectionDetailState(
    val collectionId: Int = -1,
    val collectionName: String = "",
    val quotes: List<Quote> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)
