package com.tpp.theperiodpurse.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDAO {
    @Query("SELECT * FROM dates WHERE id = :id")
    fun get(id: Int): Date
    @Query("SELECT * FROM dates")
    fun getDates(): Flow<List<Date>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(date: Date)
    @Update
    suspend fun update(date: Date)
    @Delete
    suspend fun delete(date: Date)
}