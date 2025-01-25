package com.tpp.theperiodpurse.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.tpp.theperiodpurse.data.model.Symptom
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.state.CalendarUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUIState(getDayInfo(), Symptom.FLOW))
    val uiState: StateFlow<CalendarUIState> = _uiState.asStateFlow()

    private fun getDayInfo(): LinkedHashMap<LocalDate, CalendarDayUIState> {
        return LinkedHashMap()
    }

    fun setDayInfo(day: LocalDate, dayUIState: CalendarDayUIState) {
        _uiState.value.days[day] = dayUIState
        _uiState.update { state -> CalendarUIState(state.days, state.selectedSymptom) }
    }

    fun updateDayInfo(day: LocalDate, dayUIState: CalendarDayUIState) {
        val original = _uiState.value.days[day]
        var newState = dayUIState
        if (original != null) {
            newState = CalendarDayUIState(
                flow = dayUIState.flow ?: original.flow,
                mood = dayUIState.mood ?: original.mood,
                exerciseLengthString = if (dayUIState.exerciseLengthString != "") {
                    dayUIState.exerciseLengthString
                } else original.exerciseLengthString,
                exerciseType = dayUIState.exerciseType ?: original.exerciseType,
                crampSeverity = dayUIState.crampSeverity ?: original.crampSeverity,
                sleepString = if (dayUIState.sleepString != "") {
                    dayUIState.sleepString
                } else original.sleepString,
                ovulation = dayUIState.ovulation
            )
        }
        _uiState.value.days[day] = newState

        _uiState.update { state -> state }
    }

    fun clearFlow(day: LocalDate) {
        val original = _uiState.value.days[day]
        if (original != null) {
            val newState = CalendarDayUIState(
                flow = null,
                mood = original.mood,
                exerciseLengthString = original.exerciseLengthString,
                exerciseType = original.exerciseType,
                crampSeverity = original.crampSeverity,
                sleepString = original.sleepString,
            )
            _uiState.value.days[day] = newState
        }

        _uiState.update { state -> state }
    }

    fun setSelectedSymptom(symptom: Symptom) {
        _uiState.update { state -> CalendarUIState(state.days, symptom) }
    }
}
