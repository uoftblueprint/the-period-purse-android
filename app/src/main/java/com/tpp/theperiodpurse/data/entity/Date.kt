package com.tpp.theperiodpurse.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.data.helper.DaysConverter
import com.tpp.theperiodpurse.data.helper.DurationConverter
import com.tpp.theperiodpurse.data.model.CrampSeverity
import com.tpp.theperiodpurse.data.model.Exercise
import com.tpp.theperiodpurse.data.model.FlowSeverity
import com.tpp.theperiodpurse.data.model.Mood
import com.tpp.theperiodpurse.data.model.Ovulation
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
    val ovulating: Ovulation? = null
)
