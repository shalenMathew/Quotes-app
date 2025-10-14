package com.shalenmathew.quotesapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_quotes")
data class CustomQuote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quote: String,
    val author: String,
    val createdAt: Long = System.currentTimeMillis()
)

fun CustomQuote.toQuote(): Quote = Quote(
    id = id,
    quote = quote,
    author = author,
    liked = false,
    updatedAt = createdAt
)
