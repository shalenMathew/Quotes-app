package com.shalenmathew.quotesapp.presentation.screens.library.util

import com.shalenmathew.quotesapp.domain.model.Quote

sealed class CollectionDetailEvent {
    data class LoadCollection(val id: Int) : CollectionDetailEvent()
    data class OnSearchQueryChanged(val query: String) : CollectionDetailEvent()
    data class Like(val quote: Quote) : CollectionDetailEvent()
    data class Delete(val quote: Quote) : CollectionDetailEvent()
}
