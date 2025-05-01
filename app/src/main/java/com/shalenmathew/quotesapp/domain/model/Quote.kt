package com.shalenmathew.quotesapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class Quote(
    @PrimaryKey()
    val id:Int?=null,
    val quote: String,
    val author:String,
    var liked:Boolean,
): Parcelable