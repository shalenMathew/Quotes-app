package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class GetCollectionById(private val repository: CollectionRepository) {
    suspend operator fun invoke(id: Int): Collection? = repository.getCollectionById(id)
}
