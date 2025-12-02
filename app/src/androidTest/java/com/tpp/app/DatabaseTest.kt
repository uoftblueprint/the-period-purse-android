package com.tpp.app

import com.tpp.app.data.repository.DateRepository
import com.tpp.app.data.repository.UserRepository
import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest
class DatabaseTest {
    private lateinit var dateRepository: DateRepository
    private lateinit var userRepository: UserRepository

//    Todo: finish tests for OnboardViewModel and database testing with sample data
}
