package com.tpp.theperiodpurse.data

import androidx.room.*
import com.tpp.theperiodpurse.data.entity.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun get(id: Int): User

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    @Query("UPDATE users SET symptomsToTrack = :symptoms WHERE id = :id")
    suspend fun updateSymptoms(id: Int, symptoms: String)

    @Query("UPDATE users SET darkMode = :darkMode WHERE id = :id")
    suspend fun toggleColorMode(id: Int, darkMode: Boolean)

    @Query("UPDATE users SET allowReminders = :allowReminder WHERE id = :id")
    suspend fun updateReminders(id: Int, allowReminder: Boolean)

    @Query("UPDATE users SET allowOvulationNotifications = :allowOvulationNotification WHERE id = :id")
    suspend fun updateOvulationNotifications(id: Int, allowOvulationNotification: Boolean)

    @Query("UPDATE users SET reminderTime = :time WHERE id = :id")
    suspend fun updateReminderTime(id: Int, time: String)

    @Query("UPDATE users SET reminderFreq = :freq WHERE id = :id")
    suspend fun updateReminderFreq(id: Int, freq: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}
