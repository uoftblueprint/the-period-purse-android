package com.theperiodpurse.app.data.helper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.theperiodpurse.app.data.entity.Date
import com.theperiodpurse.app.data.model.Symptom
import java.time.Duration

class SymptomConverter {
    @TypeConverter
    fun fromSymptomList(symptomList: ArrayList<Symptom>): String {
        val gson = Gson()
        return gson.toJson(symptomList)
    }

    @TypeConverter
    fun toSymptomList(symptomList: String): ArrayList<Symptom> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Symptom>>() {}.type
        return gson.fromJson(symptomList, type)
    }
}

class DateConverter {
    @TypeConverter
    fun fromDateList(dateList: ArrayList<Date>): String {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Date>>() {}.type
        return gson.toJson(dateList, type)
    }

    @TypeConverter
    fun toDateList(dateList: String): ArrayList<Date> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Date>>() {}.type
        return gson.fromJson(dateList, type)
    }
}

class DaysConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return if (value == null) null else java.util.Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}

class DurationConverter {
    @TypeConverter
    fun fromDuration(value: Duration?): Long? {
        return value?.toMinutes()
    }

    @TypeConverter
    fun toDuration(value: Long?): Duration? {
        return value?.let { Duration.ofMinutes(it) }
    }
}
