package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class UpdateCollection(private val repository: CollectionRepository) {
    suspend operator fun invoke(collection: Collection) = repository.updateCollection(collection)
}
