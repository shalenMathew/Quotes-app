package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class RemoveQuoteFromCollection(private val repository: CollectionRepository) {
    suspend operator fun invoke(collectionId: Int, quoteId: Int, isCustom: Boolean) {
        repository.removeQuoteFromCollection(collectionId, quoteId, isCustom)
    }
}
