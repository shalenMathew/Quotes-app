package com.shalenmathew.quotesapp.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "collection_quote_cross_ref",
    primaryKeys = ["collectionId", "quoteId", "isCustom"],
    foreignKeys = [
        ForeignKey(
            entity = Collection::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("collectionId")]
)
data class CollectionQuoteCrossRef(
    val collectionId: Int,
    val quoteId: Int,
    val isCustom: Boolean
)
