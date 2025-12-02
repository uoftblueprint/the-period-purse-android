package com.theperiodpurse.app.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.core.DayPosition
import com.theperiodpurse.app.data.model.Symptom
import com.theperiodpurse.app.navigateToLogScreenWithDate
import com.theperiodpurse.app.ui.calendar.components.MonthHeader
import com.theperiodpurse.app.ui.state.CalendarUIState
import com.theperiodpurse.app.ui.viewmodel.AppViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(
    calendarContentDescription: String,
    state: CalendarState,
    calendarUIState: CalendarUIState,
    selectedSymptom: Symptom,
    navController: NavController,
    appViewModel: AppViewModel
) {
    VerticalCalendar(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .semantics { contentDescription = calendarContentDescription },
        contentPadding = PaddingValues(bottom = 120.dp),
        state = state,
        monthHeader = { month ->
            MonthHeader(month, appViewModel)
        },
        dayContent = { day ->
            if (day.position == DayPosition.MonthDate) {
                CalendarDay(
                    day = day,
                    calendarDayUIState = calendarUIState.days[day.date],
                    activeSymptom = selectedSymptom,
                    onClick = { navigateToLogScreenWithDate(day.date, navController) },
                    appViewModel = appViewModel
                )
            }
        },
    )
}
