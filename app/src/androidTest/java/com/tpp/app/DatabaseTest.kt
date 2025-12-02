package com.theperiodpurse.app

import com.theperiodpurse.app.data.repository.DateRepository
import com.theperiodpurse.app.data.repository.UserRepository
import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest
class DatabaseTest {
    private lateinit var dateRepository: DateRepository
    private lateinit var userRepository: UserRepository

//    Todo: finish tests for OnboardViewModel and database testing with sample data
}
