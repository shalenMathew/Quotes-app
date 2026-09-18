package com.shalenmathew.quotesapp.presentation.screens.library.util

import com.shalenmathew.quotesapp.domain.model.Collection

data class LibraryState(
    val collections: List<Collection> = emptyList(),
    val selectedQuoteCollectionIds: List<Int> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
