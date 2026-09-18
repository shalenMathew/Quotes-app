package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow

class GetCollectionIdsForQuote(private val repository: CollectionRepository) {
    operator fun invoke(quoteId: Int, isCustom: Boolean): Flow<List<Int>> = repository.getCollectionIdsForQuote(quoteId, isCustom)
}
