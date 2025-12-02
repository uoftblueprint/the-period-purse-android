package com.theperiodpurse.app.ui.state

import com.theperiodpurse.app.data.entity.Date
import com.theperiodpurse.app.data.model.Symptom

// data class
data class AppUiState(
    // Preffered Symptoms to be tracked
    var trackedSymptoms: List<Symptom> = listOf(),
    var allowReminders: Boolean = false,
    var reminderFrequency: String = "Every day",
    var reminderTime: String = "12:00 PM",
    var dates: List<Date> = emptyList(),
    var darkMode: Boolean = false,
)
