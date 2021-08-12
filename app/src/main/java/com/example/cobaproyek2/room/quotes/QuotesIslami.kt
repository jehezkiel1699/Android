package com.example.cobaproyek2.room.quotes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Quotes_Tb")
data class QuotesIslami (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val quotes: String,
    val liked: String
)