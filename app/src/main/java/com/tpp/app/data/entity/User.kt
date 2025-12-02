package com.theperiodpurse.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.theperiodpurse.app.data.helper.DateConverter
import com.theperiodpurse.app.data.helper.SymptomConverter
import com.theperiodpurse.app.data.model.Symptom

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @TypeConverters(SymptomConverter::class)
    val symptomsToTrack: ArrayList<Symptom> = ArrayList(),
    @TypeConverters(DateConverter::class)
    val periodHistory: ArrayList<Date> = ArrayList(),
    val averagePeriodLength: Int = 0,
    val averageCycleLength: Int = 0,
    val daysSinceLastPeriod: Int = 0,
    val allowReminders: Boolean = false,
    val darkMode: Boolean = false,
    val reminderTime: String = "12:00 PM",
    val reminderFreq: String = "Every day",
)
