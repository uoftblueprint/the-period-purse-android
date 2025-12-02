package com.theperiodpurse.app.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.theperiodpurse.app.data.*
import com.theperiodpurse.app.data.helper.DaysConverter
import com.theperiodpurse.app.data.helper.DurationConverter
import com.theperiodpurse.app.data.model.CrampSeverity
import com.theperiodpurse.app.data.model.Exercise
import com.theperiodpurse.app.data.model.FlowSeverity
import com.theperiodpurse.app.data.model.Mood
import java.time.Duration
import java.util.Date

@Entity(tableName = "dates")
data class Date(
    @PrimaryKey
    @TypeConverters(DaysConverter::class)
    val date: Date,
    val flow: FlowSeverity?,
    val mood: Mood?,
    @TypeConverters(DurationConverter::class)
    val exerciseLength: Duration?,
    val exerciseType: Exercise?,
    val crampSeverity: CrampSeverity?,
    @TypeConverters(DurationConverter::class)
    val sleep: Duration?,
    val notes: String,
)
