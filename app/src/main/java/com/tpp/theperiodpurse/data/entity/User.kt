package com.tpp.theperiodpurse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tpp.theperiodpurse.data.helper.DateConverter
import com.tpp.theperiodpurse.data.helper.SymptomConverter
import com.tpp.theperiodpurse.data.model.Symptom

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
    val allowOvulationNotifications: Boolean = false,
    val averageOvulationPhaseLength: Int = 0,
    val averageTimeBetweenPeriodAndOvulation: Int = 0
)
