package com.example.cobaproyek2.room.quotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(QuotesIslami::class), version = 1)
abstract class QuotesIslamiDB: RoomDatabase() {
    abstract fun QuotesIslamiDAO(): quotesIslamiDAO
    companion object{
        private var INSTANCE: QuotesIslamiDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE
            ?: synchronized(LOCK){
            INSTANCE
                ?: buildDatabase(
                    context
                ).also{
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            QuotesIslamiDB::class.java, "dbQuotes.db"
        ).build()
    }

}