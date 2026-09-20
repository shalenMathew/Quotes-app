package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class GetCollectionByName(private val repository: CollectionRepository) {
    suspend operator fun invoke(name: String): Collection? = repository.getCollectionByName(name)
}
