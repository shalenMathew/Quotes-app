package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow

class GetAllCollections(private val repository: CollectionRepository) {
    operator fun invoke(): Flow<List<Collection>> = repository.getAllCollections()
}
