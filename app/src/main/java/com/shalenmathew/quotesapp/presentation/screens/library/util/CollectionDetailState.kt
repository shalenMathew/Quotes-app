package com.shalenmathew.quotesapp.presentation.screens.library.util

import com.shalenmathew.quotesapp.domain.model.CollectionType
import com.shalenmathew.quotesapp.domain.model.Quote

data class CollectionDetailState(
    val collectionId: Int = -1,
    val collectionType: CollectionType = CollectionType.Favorites,
    val collectionName: String = "",
    val quotes: List<Quote> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)
