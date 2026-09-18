package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class IsQuoteInCollection(private val repository: CollectionRepository) {
    suspend operator fun invoke(collectionId: Int, quoteId: Int, isCustom: Boolean): Boolean {
        return repository.isQuoteInCollection(collectionId, quoteId, isCustom)
    }
}
