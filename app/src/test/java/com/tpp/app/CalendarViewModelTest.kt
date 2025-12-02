package com.theperiodpurse.app

import com.theperiodpurse.app.data.model.CrampSeverity
import com.theperiodpurse.app.data.model.Exercise
import com.theperiodpurse.app.data.model.FlowSeverity
import com.theperiodpurse.app.data.model.Mood
import com.theperiodpurse.app.ui.state.CalendarDayUIState
import com.theperiodpurse.app.ui.viewmodel.CalendarViewModel
import org.junit.Test
import java.time.LocalDate

class CalendarViewModelTest {
    private val viewModel = CalendarViewModel()
    private val originalDayUIState = CalendarDayUIState(
        FlowSeverity.Heavy,
        Mood.ANGRY,
        "11:00:00",
        Exercise.CARDIO,
        CrampSeverity.Bad,
        "08:00:00",
    )
    private val date = LocalDate.of(2020, 1, 1)

    @Test
    fun calendarViewModel_SetDayInfo() {
        assert(!viewModel.uiState.value.days.containsKey(date))
        viewModel.setDayInfo(date, originalDayUIState)
        assert(viewModel.uiState.value.days[date] == originalDayUIState)
    }

    @Test
    fun calendarViewModel_UpdateDayInfo() {
        viewModel.setDayInfo(date, originalDayUIState)
        assert(viewModel.uiState.value.days[date]?.flow == originalDayUIState.flow)
        val newDayUIState = CalendarDayUIState(FlowSeverity.None)
        viewModel.updateDayInfo(date, newDayUIState)
        assert(viewModel.uiState.value.days[date]?.flow == FlowSeverity.None)
    }
}
