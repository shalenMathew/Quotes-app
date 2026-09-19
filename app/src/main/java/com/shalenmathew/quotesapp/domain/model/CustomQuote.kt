package com.shalenmathew.quotesapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "custom_quotes")
@Parcelize
data class CustomQuote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quote: String,
    val author: String,
    val createdAt: Long = System.currentTimeMillis()
): Parcelable

fun CustomQuote.toQuote(): Quote = Quote(
    id = id,
    quote = quote,
    author = author,
    liked = false,
    updatedAt = createdAt,
    isCustom = true
)
