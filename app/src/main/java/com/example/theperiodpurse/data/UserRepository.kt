package com.example.theperiodpurse.data

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(private val userDAO: UserDAO) {
    private val allUsers = MutableLiveData<List<User>>()
    val user = MutableLiveData<User>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addUser(user: User) {
        coroutineScope.launch (Dispatchers.IO) {
            userDAO.save(user)
        }
    }

    suspend fun getUser(id: Int): User {
        return userDAO.get(id)
    }

    fun getAllUsers() {
        coroutineScope.launch (Dispatchers.IO) {
            allUsers.postValue(userDAO.getUsers())
        }
    }
}