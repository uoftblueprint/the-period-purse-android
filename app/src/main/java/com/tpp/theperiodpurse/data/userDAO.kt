package com.tpp.theperiodpurse.data

import androidx.room.*

@Dao
interface UserDAO {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun get(id: Int): User
    @Query("SELECT * FROM users")
    fun getUsers(): List<User>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User)
    @Update
    suspend fun update(user: User)
    @Delete
    suspend fun delete(user: User)
}