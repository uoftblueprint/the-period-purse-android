package com.tpp.theperiodpurse.ui.symptomlog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.model.FlowSeverity
import com.tpp.theperiodpurse.data.model.Ovulation
import com.tpp.theperiodpurse.ui.calendar.components.Day
import com.tpp.theperiodpurse.ui.calendar.components.MonthHeader
import com.tpp.theperiodpurse.ui.component.PopupTopBar
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.theme.Red
import com.tpp.theperiodpurse.ui.theme.SelectedColor1
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date.from

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogMultipleDatesScreen(
    onClose: () -> Unit,
    calendarViewModel: CalendarViewModel,
    appViewModel: AppViewModel,
) {
    val context = LocalContext.current.applicationContext
    Image(
        painter = painterResource(appViewModel.colorPalette.background),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Cycle Page" },
        contentScale = ContentScale.FillBounds,
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        PopupTopBar(onClose = onClose, appViewModel = appViewModel) {
            LogMultipleDatesText(appViewModel)
        }
        val currentMonth = remember { YearMonth.now() }
        val startMonth = remember { currentMonth.minusMonths(12) } // Previous months
        val endMonth = remember { currentMonth.plusMonths(0) } // Next months
        val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = firstDayOfWeek,
        )

        val calendarDayUIStates = calendarViewModel.uiState.collectAsState().value.days

        val selectedDates = mutableSetOf<LocalDate>()
        val unselectedDates = mutableSetOf<LocalDate>()

        Box {
            VerticalCalendar(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .semantics { contentDescription = "Calendar" },
                contentPadding = PaddingValues(bottom = 48.dp),
                state = state,
                monthHeader = { month ->
                    MonthHeader(month, appViewModel)
                },
                dayContent = { day ->
                    if (day.position == DayPosition.MonthDate) {
                        val initSelected = calendarDayUIStates[day.date]?.flow != null &&
                                calendarDayUIStates[day.date]?.flow != FlowSeverity.Predicted
                        var selected by remember { mutableStateOf(initSelected) }
//                        val (dayColor, _) = getDayColorAndIcon(Symptom.FLOW, calendarDayUIStates[day.date])
                        Day(
                            day.date,
                            color = if (selected) Red else appViewModel.colorPalette.CalendarDayColor,
                            iconId = null,
                            onClick = {
                                selected = !selected
                                if (selected) {
                                    selectedDates.add(day.date)
                                    unselectedDates.remove(day.date)
                                } else {
                                    selectedDates.remove(day.date)
                                    unselectedDates.add(day.date)
                                }
                            },
                        )
                    }
                },
            )

            FloatingActionButton(
                onClick = {
                    selectedDates.forEach {
                        appViewModel.saveDate(
                            Date(
                                date = from(it.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                flow = FlowSeverity.Medium,
                                ovulating = false,
                                exerciseType = null,
                                exerciseLength = null,
                                crampSeverity = null,
                                sleep = null,
                                mood = null,
                                notes = "",
                            ),
                            context,
                        )
                        calendarViewModel.updateDayInfo(it, CalendarDayUIState(flow = FlowSeverity.Medium))
                    }
                    val converted = unselectedDates.toList().map {
                        from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    }
                    unselectedDates.forEach {
                        calendarViewModel.clearFlow(it)
                    }
                    if (converted.isNotEmpty()) {
                        appViewModel.deleteManyDates(converted, context)
                    }
                    onClose()
                },
                backgroundColor = SelectedColor1,
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomEnd),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = "Save",
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun LogMultipleDatesText(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.log_multiple_dates_header_title),
            fontSize = 20.scaledSp(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier,
            color = appViewModel.colorPalette.MainFontColor
        )
        Text(
            stringResource(R.string.log_multiple_dates_header_body),
            fontSize = 13.scaledSp(),
            modifier = modifier,
            color = appViewModel.colorPalette.MainFontColor
        )
    }
}
