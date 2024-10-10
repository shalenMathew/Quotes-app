package com.example.quotesapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Quote(
    @PrimaryKey()
    val id:Int?=null,
    val quote: String,
    val author:String,
    var liked:Boolean,
)