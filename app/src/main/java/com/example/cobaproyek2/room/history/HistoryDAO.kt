package com.example.cobaproyek2.room.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun tambahHistory(history: History)

    @Query("SELECT * FROM History_Tb ORDER BY id ASC")
    suspend fun getAllHistory(): List<History>

//    @Query("SELECT * FROM History_Tb ORDER BY RANDOM() limit 1")
//    suspend fun getRandomQuotes(): List<History>

    @Query("SELECT max(id) FROM History_Tb")
    suspend fun getMaxID(): Int

    @Query("SELECT * FROM History_Tb WHERE namasolat LIKE :namasolat AND tgl LIKE :tgl AND waktu LIKE :waktu")
    suspend fun getCheckHistory(namasolat:String, tgl:String, waktu:String):List<History>

}