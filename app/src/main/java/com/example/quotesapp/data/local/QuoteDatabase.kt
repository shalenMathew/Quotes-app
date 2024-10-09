package com.example.quotesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quotesapp.domain.model.Quote


@Database(entities = [Quote::class], version = 1)
abstract class QuoteDatabase:RoomDatabase() {

    abstract fun getQuoteDao():QuoteDao

}