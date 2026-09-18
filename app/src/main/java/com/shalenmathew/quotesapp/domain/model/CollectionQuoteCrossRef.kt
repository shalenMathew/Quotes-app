package com.shalenmathew.quotesapp.domain.model

import androidx.room.Entity

@Entity(tableName = "collection_quote_cross_ref", primaryKeys = ["collectionId", "quoteId", "isCustom"])
data class CollectionQuoteCrossRef(
    val collectionId: Int,
    val quoteId: Int,
    val isCustom: Boolean
)
