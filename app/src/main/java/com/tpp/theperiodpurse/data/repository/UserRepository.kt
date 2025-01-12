package com.tpp.theperiodpurse.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tpp.theperiodpurse.data.ApplicationRoomDatabase
import com.tpp.theperiodpurse.data.UserDAO
import com.tpp.theperiodpurse.data.entity.User
import com.tpp.theperiodpurse.data.model.Symptom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepository(private val userDAO: UserDAO) {
    val isOnboarded: MutableLiveData<Boolean?> = MutableLiveData(null)
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addUser(user: User, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            ApplicationRoomDatabase.getDatabase(context).userDAO().save(user)
        }
    }

    fun setSymptoms(symptoms: List<Symptom>, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            val jsonSymptoms = Gson().toJson(symptoms)
            if (jsonSymptoms != null) {
                ApplicationRoomDatabase.getDatabase(context).userDAO().updateSymptoms(symptoms = jsonSymptoms, id = 1)
            }
        }
    }

    fun setColorMode(darkMode: Boolean, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            ApplicationRoomDatabase.getDatabase(context).userDAO().toggleColorMode(id = 1, darkMode)
        }
    }

    fun setReminders(allowReminders: Boolean, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            ApplicationRoomDatabase.getDatabase(context).userDAO().updateReminders(id = 1, allowReminders)
        }
    }

    fun setReminderTime(time: String, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            ApplicationRoomDatabase.getDatabase(context).userDAO().updateReminderTime(id = 1, time)
        }
    }

    fun setReminderFreq(freq: String) {
        coroutineScope.launch(Dispatchers.IO) {
            userDAO.updateReminderFreq(id = 1, freq)
        }
    }

    fun setOvulationNotifications(allowOvulationNotifications: Boolean, context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
            ApplicationRoomDatabase.getDatabase(context).userDAO().updateOvulationNotifications(id = 1, allowOvulationNotifications)
        }
    }

    suspend fun getUser(id: Int, context: Context): User {
        return ApplicationRoomDatabase.getDatabase(context).userDAO().get(id)
    }

    suspend fun isEmpty(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            ApplicationRoomDatabase.getDatabase(context).userDAO()
                .getUsers()
                .isEmpty()
        }
    }

    suspend fun isOnboarded(context: Context) {
        isOnboarded.postValue(ApplicationRoomDatabase.getDatabase(context).userDAO().getUsers().isNotEmpty())
    }

}
