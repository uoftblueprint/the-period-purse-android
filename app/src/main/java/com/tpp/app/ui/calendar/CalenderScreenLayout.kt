package com.tpp.app.ui.calendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.tpp.app.R
import com.tpp.app.utility.getPeriodPrediction
import com.tpp.app.data.model.FlowSeverity
import com.tpp.app.data.model.Symptom
import com.tpp.app.ui.calendar.components.SymptomTab
import com.tpp.app.ui.state.CalendarDayUIState
import com.tpp.app.ui.viewmodel.AppViewModel
import com.tpp.app.ui.viewmodel.CalendarViewModel
import java.time.YearMonth
import java.time.ZoneId

val previewTrackedSymptoms = Symptom.values().asList()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreenLayout(
    calendarViewModel: CalendarViewModel,
    navController: NavController,
    appViewModel: AppViewModel,
) {
    val calendarUIState by calendarViewModel.uiState.collectAsState()
    // Contains the swappable content
    // TODO: upon render, update calendar ui state so that predicted periods are calculated and
    //  the corresponding day ui is updated. Need to check how expensive this is.
    Log.d("Calendar Screen", "Renderng calendar screen")
    val bg = painterResource(appViewModel.colorPalette.background)
    val appUiState by appViewModel.uiState.collectAsState()

    val selectedSymptom = calendarUIState.selectedSymptom
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) } // Previous months
    val endMonth = remember { currentMonth.plusMonths(1) } // Next months
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val dates = ArrayList(appViewModel.getDates())
    // this is very inefficient, but should work for the time being
    calendarUIState.days.forEach{
        if (it.value.flow == FlowSeverity.Predicted) {
            calendarViewModel.clearFlow(it.key)
        }
    }

    // clear out days previous predicted
    if (dates.isNotEmpty()) {
        val predictedPeriodDates = getPeriodPrediction(ArrayList(appViewModel.getDates()))

        // update the color for these dates
        predictedPeriodDates.forEach{
            val localDate = it.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
            if (localDate != null) {
                calendarViewModel.setDayInfo(localDate, CalendarDayUIState(FlowSeverity.Predicted))
            }
        }
    }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )
    var calendarContentDescription = stringResource(id = R.string.calendar_page)
    Box {
        Image(
            painter = bg,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = calendarContentDescription },
            contentScale = ContentScale.FillBounds,
        )
        Column {
            SymptomTab(
                selectedSymptom = selectedSymptom,
                onSymptomClick = { calendarViewModel.setSelectedSymptom(it) },
                trackedSymptoms = appUiState.trackedSymptoms,
                appViewModel = appViewModel
            )
            calendarContentDescription = stringResource(id = R.string.calendar)
            Calendar(
                calendarContentDescription = calendarContentDescription,
                state = state,
                calendarUIState = calendarUIState,
                selectedSymptom = selectedSymptom,
                navController = navController,
                appViewModel = appViewModel
            )
        }
    }
}
