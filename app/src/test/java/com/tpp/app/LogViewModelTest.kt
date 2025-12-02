package com.theperiodpurse.app

import com.theperiodpurse.app.data.model.*
import com.theperiodpurse.app.ui.state.CalendarDayUIState
import com.theperiodpurse.app.ui.viewmodel.LogViewModel
import org.junit.Test

class LogViewModelTest {
    private val logSquarePrompts = listOf( // log prompts that use selectable squares
        LogPrompt.Flow,
        LogPrompt.Mood,
        LogPrompt.Cramps,
        LogPrompt.Sleep,
        LogPrompt.Exercise,
        LogPrompt.Notes,
    )
    private val viewModel = LogViewModel(logSquarePrompts)

    @Test
    fun logViewModel_UpdateUIState() {
        val flow = FlowSeverity.Light; val cramp = CrampSeverity.Bad
        val mood = Mood.SAD; val exercise = Exercise.CYCLING
        val exerciseString = "10:01:00"; val sleepString = "11:00:00"
        val state = CalendarDayUIState(
            flow = flow,
            mood = mood,
            exerciseLengthString = exerciseString,
            crampSeverity = cramp,
            exerciseType = exercise,
            sleepString = sleepString,
        )
        viewModel.populateWithUIState(state)
        val selectSquares = viewModel.uiState.value.selectSquares
        val promptToText = viewModel.uiState.value.promptToText
        assert(selectSquares[LogPrompt.Flow.title] == flow.displayName)
        assert(selectSquares[LogPrompt.Cramps.title] == cramp.displayName)
        assert(selectSquares[LogPrompt.Exercise.title] == exercise.displayName)
        assert(promptToText[LogPrompt.Sleep.title] == sleepString)
        assert(promptToText[LogPrompt.Exercise.title] == exerciseString)
    }
}
