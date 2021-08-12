package com.example.cobaproyek2.room.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "History_Tb")
data class History (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val namasolat : String,
    val tgl: String,
    val waktu : String
)