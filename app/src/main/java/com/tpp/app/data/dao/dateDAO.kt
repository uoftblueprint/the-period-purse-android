package com.tpp.app.data

import androidx.room.*
import com.tpp.app.data.entity.Date
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDAO {
    @Query("SELECT * FROM dates")
    fun getDates(): Flow<List<Date>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(date: Date)

    @Update
    suspend fun update(date: Date)

    @Delete
    suspend fun delete(date: Date)

    @Query("DELETE FROM dates WHERE date in (:dates)")
    suspend fun deleteMany(dates: List<Long>?)
}
