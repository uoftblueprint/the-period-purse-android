package com.theperiodpurse.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.theperiodpurse.app.data.model.Symptom
import com.theperiodpurse.app.ui.state.SettingsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    appViewModel: AppViewModel,
) : ViewModel() {

    val symptoms = appViewModel.getTrackedSymptoms()
    private val _uiState = MutableStateFlow(SettingsUIState(symptoms))
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    fun updateSymptoms(symptom: Symptom): Boolean {
        val sympCopy = symptoms.toMutableList()
        return if (sympCopy.contains(symptom)) {
            sympCopy.remove(symptom)
            _uiState.update { currentState -> currentState.copy(symptomsOptions = sympCopy) }
            false
        } else {
            sympCopy.add(symptom)
            _uiState.update { currentState -> currentState.copy(symptomsOptions = sympCopy) }
            true
        }
    }

    fun isSymptomChecked(symptom: Symptom): Boolean {
        return symptoms.contains(symptom)
    }

    fun toggleallowReminders() {
        val currentReminderState = _uiState.value.allowReminders
        _uiState.update { currentState -> currentState.copy(allowReminders = !currentReminderState) }
    }
}
