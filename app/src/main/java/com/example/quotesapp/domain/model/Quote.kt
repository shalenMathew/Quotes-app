package com.example.quotesapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val quote: String,
    val author:String,
    var liked:Boolean,
)