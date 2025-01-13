package com.tpp.theperiodpurse.data.repository

import android.content.Context
import android.util.Log
import com.tpp.theperiodpurse.data.ApplicationRoomDatabase
import com.tpp.theperiodpurse.data.dao.DateDAO
import com.tpp.theperiodpurse.data.entity.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DateRepository(private val dateDAO: DateDAO) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addDate(date: Date, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = ApplicationRoomDatabase.getDatabase(context)
            Log.d("DateRepository", "Adding date to database with id $database")
            database.dateDAO().save(date)
        }
    }

    suspend fun getAllDates(context: Context): Flow<List<Date>> {
        return withContext(Dispatchers.IO) {
            ApplicationRoomDatabase.getDatabase(context).dateDAO().getDates()
        }
    }

    fun deleteDate(date: Date, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                ApplicationRoomDatabase.getDatabase(context).dateDAO().delete(date)
            }
        }
    }

    fun deleteManyDates(dates: List<Long>, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = ApplicationRoomDatabase.getDatabase(context)
            database.dateDAO().deleteMany(dates)
        }
    }
}
