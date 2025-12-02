package com.theperiodpurse.app.ui.state

import com.theperiodpurse.app.data.model.Symptom

data class SettingsUIState(

    /** Available Symptoms to track*/
    val symptomsOptions: List<Symptom>,
    val allowReminders: Boolean = false,
    var darkMode: Boolean = false,
)
