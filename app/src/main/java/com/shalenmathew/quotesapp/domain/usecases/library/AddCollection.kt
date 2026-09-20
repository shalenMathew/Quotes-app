package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class AddCollection(private val repository: CollectionRepository) {
    suspend operator fun invoke(name: String): Result<Unit> {
        val trimmedName = name.trim()
        
        if (trimmedName.isBlank()) {
            return Result.failure(Exception("Name cannot be empty"))
        }

        if (trimmedName.equals("Favorites", ignoreCase = true) || 
            trimmedName.equals("Custom", ignoreCase = true)) {
            return Result.failure(Exception("Reserved name"))
        }

        val existing = repository.getCollectionByName(trimmedName)
        if (existing != null) {
            return Result.failure(Exception("Collection already exists"))
        }

        repository.insertCollection(Collection(name = trimmedName))
        return Result.success(Unit)
    }
}
