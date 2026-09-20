package com.shalenmathew.quotesapp.presentation.screens.library.util

import com.shalenmathew.quotesapp.domain.model.Collection

sealed class LibraryEvent {
    data class AddCollection(val name: String) : LibraryEvent()
    data class UpdateCollection(val collection: Collection) : LibraryEvent()
    data class DeleteCollection(val collection: Collection) : LibraryEvent()
    data class AddQuoteToCollection(val collectionId: Int, val quoteId: Int, val isCustom: Boolean) : LibraryEvent()
    data class RemoveQuoteFromCollection(val collectionId: Int, val quoteId: Int, val isCustom: Boolean) : LibraryEvent()
    data class SetSelectedQuote(val quoteId: Int?, val isCustom: Boolean) : LibraryEvent()
    object ClearError : LibraryEvent()
}
