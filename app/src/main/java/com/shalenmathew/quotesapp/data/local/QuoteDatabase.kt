package com.shalenmathew.quotesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote


@Database(entities = [Quote::class, CustomQuote::class], version = 6)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun getQuoteDao(): QuoteDao
    abstract fun getCustomQuoteDao(): CustomQuoteDao

}