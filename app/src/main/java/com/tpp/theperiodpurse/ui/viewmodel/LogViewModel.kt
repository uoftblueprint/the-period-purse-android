package com.tpp.theperiodpurse.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.tpp.theperiodpurse.data.model.*
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.state.LogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LogViewModel(val logPrompts: List<LogPrompt>) : ViewModel() {
    private val _uiState = MutableStateFlow(
        LogUiState(
            getSquares(logPrompts),
            getText(logPrompts),
        ),
    )
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    fun populateWithUIState(dayUIState: CalendarDayUIState) {
        _uiState.update { currentState ->
            val selectSquares = currentState.selectSquares
            if (dayUIState.crampSeverity != null) {
                selectSquares[LogPrompt.Cramps.title] = dayUIState.crampSeverity.displayName
            }
            if (dayUIState.flow != null) {
                selectSquares[LogPrompt.Flow.title] = dayUIState.flow.displayName
            }
            if (dayUIState.ovulating != null) {
                selectSquares[LogPrompt.Ovulation.title] = dayUIState.ovulating.displayName
            }
            if (dayUIState.mood != null) {
                selectSquares[LogPrompt.Mood.title] = dayUIState.mood.displayName
            }
            if (dayUIState.exerciseType != null) {
                selectSquares[LogPrompt.Exercise.title] = dayUIState.exerciseType.displayName
            }
            if (dayUIState.exerciseLengthString != "") {
                currentState.promptToText[LogPrompt.Exercise.title] = dayUIState.exerciseLengthString
            }
            if (dayUIState.sleepString != "") {
                currentState.promptToText[LogPrompt.Sleep.title] = dayUIState.sleepString
            }
            currentState.copy(selectSquares = currentState.selectSquares)
        }
    }
    fun setSquareSelected(logSquare: LogSquare) {
        _uiState.update { currentState ->
            currentState.selectSquares[logSquare.promptTitle] = logSquare.dataName
            currentState.copy(
                selectSquares =
                currentState.selectSquares,
            )
        }
    }

    fun getSquareSelected(logPrompt: LogPrompt): String? {
        if (uiState.value.selectSquares[logPrompt.title] !is String) {
            return(null)
        } else {
            return(uiState.value.selectSquares[logPrompt.title] as String)
        }
    }

    fun getSelectedFlow(): FlowSeverity? {
        var selectedFlow = uiState.value.selectSquares[LogPrompt.Flow.title]
        if (selectedFlow is String) {
            if (selectedFlow == "") selectedFlow = "None"
            return FlowSeverity.getSeverityByDisplayName(selectedFlow)
        }
        return null
    }
    fun getOvulation(): Ovulation? {
        var selectedOvulation = uiState.value.selectSquares[LogPrompt.Ovulation.title]
        if (selectedOvulation is String) {
            if (selectedOvulation == "") return null
            return Ovulation.getOvulationByDisplayName(selectedOvulation)
        }
        return null
    }
    fun getSelectedCrampSeverity(): CrampSeverity? {
        var selectedCrampSeverity = uiState.value.selectSquares[LogPrompt.Cramps.title]
        if (selectedCrampSeverity is String) {
            if (selectedCrampSeverity == "") selectedCrampSeverity = "None"
            return CrampSeverity.getSeverityByDisplayName(selectedCrampSeverity)
        }
        return null
    }

    fun getSelectedMood(): Mood? {
        val selectedMood = uiState.value.selectSquares[LogPrompt.Mood.title]
        if (selectedMood is String) {
            return Mood.getMoodByDisplayName(selectedMood)
        }
        return null
    }

    fun getSelectedExercise(): Exercise? {
        val selectedExercise = uiState.value.selectSquares[LogPrompt.Exercise.title]
        if (selectedExercise is String) {
            return Exercise.getExerciseByDisplayName(selectedExercise)
        }
        return null
    }

    fun resetSquareSelected(logSquare: LogSquare) {
        _uiState.update { currentState ->
            currentState.selectSquares[logSquare.promptTitle] = false
            currentState.copy(
                selectSquares =
                currentState.selectSquares,
            )
        }
    }

    fun setText(logPrompt: LogPrompt, newText: String) {
        _uiState.update { currentState ->
            currentState.promptToText[logPrompt.title] = newText
            currentState.copy(
                selectSquares =
                currentState.selectSquares,
            )
        }
    }

    fun getText(logPrompt: LogPrompt): String {
        return(uiState.value.promptToText[logPrompt.title] ?: "")
    }

    fun resetText(logPrompt: LogPrompt) {
        _uiState.update { currentState ->
            currentState.promptToText[logPrompt.title] = ""
            currentState.copy(
                selectSquares =
                currentState.selectSquares,
            )
        }
    }

    private fun getSquares(logPrompts: List<LogPrompt>): LinkedHashMap<Int, Any> {
        val selectedSquares = LinkedHashMap<Int, Any>()
        for (logPrompt in logPrompts) {
            selectedSquares[logPrompt.title] = false
        }
        return(selectedSquares)
    }

    private fun getText(logPrompts: List<LogPrompt>): LinkedHashMap<Int, String> {
        val promptToText = LinkedHashMap<Int, String>()
        for (logPrompt in logPrompts) {
            promptToText[logPrompt.title] = ""
        }
        return(promptToText)
    }

    fun isFilled(): Boolean {
        logPrompts.forEach() { it ->
            // For Flow, Cramps, check not null and not None to return true
            // For Sleep, Excercise, Mood, check not null to return true
            // For Notes, check not empty to return true
            if (it == LogPrompt.Flow || it == LogPrompt.Cramps) {
                if (this.getSquareSelected(it) != null && this.getSquareSelected(it) != "None") {
                    return true
                }
            }
            if (it == LogPrompt.Sleep || it == LogPrompt.Mood || it == LogPrompt.Exercise) {
                if (this.getSquareSelected(it) != null) {
                    return true
                }
            }
            if (it == LogPrompt.Notes && this.getText(it) != "") {
                return true
            }
        }
        return false
    }
}
