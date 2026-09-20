package com.shalenmathew.quotesapp.domain.usecases.library

import com.shalenmathew.quotesapp.domain.model.CollectionType
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.toQuote
import com.shalenmathew.quotesapp.domain.repository.CollectionRepository
import com.shalenmathew.quotesapp.domain.repository.CustomQuoteRepository
import com.shalenmathew.quotesapp.domain.repository.FavQuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SearchQuotesInCollection(
    private val repository: CollectionRepository,
    private val favRepository: FavQuoteRepository,
    private val customQuoteRepository: CustomQuoteRepository
) {
    operator fun invoke(collectionId: Int, query: String): Flow<List<Quote>> {
        return when (CollectionType.fromId(collectionId)) {
            CollectionType.Favorites -> favRepository.getAllLikedQuotes(query)
            CollectionType.Custom -> customQuoteRepository.getAllCustomQuotes(query).map { list ->
                list.map { it.toQuote() }
            }
            is CollectionType.UserDefined -> combine(
                repository.searchQuotesInCollection(collectionId, query),
                repository.searchCustomQuotesInCollection(collectionId, query)
            ) { standard, custom ->
                standard + custom.map { it.toQuote() }
            }
        }
    }
}
