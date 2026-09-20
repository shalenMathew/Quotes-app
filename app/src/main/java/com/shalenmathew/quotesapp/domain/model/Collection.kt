package com.shalenmathew.quotesapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

import androidx.room.Index

@Entity(
    tableName = "collections",
    indices = [Index(value = ["name"], unique = true)]
)
@Parcelize
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
