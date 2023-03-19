package com.tpp.theperiodpurse.data

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(private val userDAO: UserDAO) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addUser(user: User) {
        coroutineScope.launch (Dispatchers.IO) {
            userDAO.save(user)
        }
    }

    fun setSymptoms(symptoms: List<Symptom>) {
        coroutineScope.launch (Dispatchers.IO) {
            userDAO.updateSymptoms(symptoms = symptoms, id = 1)
        }
    }

    suspend fun getUser(id: Int): User {
        return userDAO.get(id)
    }

    suspend fun isEmpty(): Boolean {
        return userDAO.getUsers().isEmpty()
    }
}