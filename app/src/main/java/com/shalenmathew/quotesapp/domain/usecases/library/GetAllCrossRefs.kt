package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.CollectionQuoteCrossRef
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository

class GetAllCrossRefs(private val repository: CollectionRepository) {
    suspend operator fun invoke(): List<CollectionQuoteCrossRef> = repository.getAllCrossRefs()
}
