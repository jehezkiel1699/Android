package com.example.cobaproyek2.room.quotes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cobaproyek2.room.quotes.QuotesIslami


@Dao
interface quotesIslamiDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun tambahQuotes(quotesIslami: QuotesIslami)

    @Query("SELECT * FROM Quotes_Tb ORDER BY id ASC")
    suspend fun getAllQuotes(): List<QuotesIslami>

    @Query("SELECT * FROM Quotes_Tb ORDER BY RANDOM() limit 1")
    suspend fun getRandomQuotes(): List<QuotesIslami>

    @Query("SELECT * FROM Quotes_Tb WHERE quotes LIKE :search")
//    '%' || :search || '%'
    suspend fun searchQuotes(search: String): List<QuotesIslami>

    @Query("SELECT max(id) FROM Quotes_Tb")
    suspend fun getMaxID(): Int

    @Query("UPDATE Quotes_Tb SET liked='y' WHERE id=:ID")
    suspend fun likeQuotes(ID: Int)

    @Query("UPDATE Quotes_Tb SET liked='n' WHERE id=:ID")
    suspend fun unlikeQuotes(ID: Int)


}