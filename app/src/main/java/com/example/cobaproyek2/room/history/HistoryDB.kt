package com.example.cobaproyek2.room.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(History::class), version = 1)
abstract class HistoryDB: RoomDatabase() {
    abstract fun HistoryDAO(): HistoryDAO
    companion object{
        private var INSTANCE: HistoryDB? = null
        private val LOCK = Any()

        operator fun invoke (context: Context) = INSTANCE?: synchronized(LOCK){
            INSTANCE?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            HistoryDB::class.java, "dbHistory.db"
        ).build()
    }

}