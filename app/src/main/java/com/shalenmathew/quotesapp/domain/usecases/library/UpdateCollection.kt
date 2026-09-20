package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class UpdateCollection(private val repository: CollectionRepository) {
    suspend operator fun invoke(collection: Collection): Result<Unit> {
        val trimmedName = collection.name.trim()
        
        if (trimmedName.isBlank()) {
            return Result.failure(Exception("Name cannot be empty"))
        }

        if (trimmedName.equals("Favorites", ignoreCase = true) || 
            trimmedName.equals("Custom", ignoreCase = true)) {
            return Result.failure(Exception("Reserved name"))
        }

        val existing = repository.getCollectionByName(trimmedName)
        if (existing != null && existing.id != collection.id) {
            return Result.failure(Exception("Another collection has this name"))
        }

        repository.updateCollection(collection.copy(name = trimmedName))
        return Result.success(Unit)
    }
}
