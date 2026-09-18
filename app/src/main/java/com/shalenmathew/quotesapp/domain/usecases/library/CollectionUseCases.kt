package com.shalenmathew.quotesapp.domain.usecases.library

data class CollectionUseCases(
    val getAllCollections: GetAllCollections,
    val addCollection: AddCollection,
    val updateCollection: UpdateCollection,
    val deleteCollection: DeleteCollection,
    val getCollectionById: GetCollectionById,
    val addQuoteToCollection: AddQuoteToCollection,
    val removeQuoteFromCollection: RemoveQuoteFromCollection,
    val isQuoteInCollection: IsQuoteInCollection,
    val getCollectionIdsForQuote: GetCollectionIdsForQuote,
    val searchQuotesInCollection: SearchQuotesInCollection
)
