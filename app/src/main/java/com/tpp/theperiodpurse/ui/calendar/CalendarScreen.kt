package com.tpp.theperiodpurse.ui.calendar


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.Screen
import com.tpp.theperiodpurse.ui.theme.HeaderColor1
import com.tpp.theperiodpurse.ui.theme.SelectedColor1
import com.tpp.theperiodpurse.data.Symptom
import com.tpp.theperiodpurse.ui.calendar.components.SymptomTab
import com.tpp.theperiodpurse.ui.calendar.components.getDayColorAndIcon
import com.tpp.theperiodpurse.ui.theme.ThePeriodPurseTheme
import com.google.accompanist.pager.*
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


val tabModifier = Modifier
    .background(Color.White)
    .fillMaxWidth()


inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<CalendarTabItem>, pagerState: PagerState) {
    // Composable for tabs
    // Tabs are displayed in ordered row by tabs list
    // PagerState allows for navigation/animation between paginated layouts
    val scope = rememberCoroutineScope()
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = HeaderColor1,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions
                    ),
                    color = SelectedColor1
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(tab.title) },
                    selected = pagerState.currentPage == index,
                    modifier = Modifier
                        .noRippleClickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
    }
}

private class NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(Color.Unspecified, true)

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleAlpha(draggedAlpha = 0f, focusedAlpha = 0f, hoveredAlpha = 0f, pressedAlpha = 0f)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(
    tabs: List<CalendarTabItem>,
    pagerState: PagerState,
    calendarViewModel: CalendarViewModel,
    navController: NavController
) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen(calendarViewModel, navController)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    calendarViewModel: CalendarViewModel
) {
    // Main calendar screen which allows navigation to cycle page and calendar
    // By default, opens on to the Calendar
    val tabs = listOf(
        CalendarTabItem.CalendarTab,
        CalendarTabItem.CycleTab
    )
    val pagerState = rememberPagerState()
    ThePeriodPurseTheme {
        Scaffold(topBar = {})
        { padding ->
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.padding(padding)
            ) {
                Tabs(tabs = tabs, pagerState = pagerState)
                TabsContent(
                    tabs = tabs,
                    pagerState = pagerState,
                    calendarViewModel = calendarViewModel,
                    navController = navController
                )
            }
        }
    }
}

val previewTrackedSymptoms = Symptom.values().asList()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreenLayout(calendarViewModel: CalendarViewModel, navController: NavController) {
        val calendarUIState by calendarViewModel.uiState.collectAsState()
    // Contains the swappable content
    ThePeriodPurseTheme {
        val bg = painterResource(R.drawable.colourwatercolour)

        var selectedSymptom = calendarUIState.selectedSymptom
        val currentMonth = remember { YearMonth.now() }
        val startMonth = remember { currentMonth.minusMonths(12) } // Previous months
        val endMonth = remember { currentMonth.plusMonths(0) } // Next months
        val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = firstDayOfWeek
        )

        Box {
            Image(
                painter = bg,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = "Calendar Page" },
                contentScale = ContentScale.FillBounds,
            )
            Column {
                SymptomTab(
                    trackedSymptoms = previewTrackedSymptoms,
                    selectedSymptom = selectedSymptom,
                    onSymptomClick = { calendarViewModel.setSelectedSymptom(it) }
//                trackedSymptoms = userDAO.get().symptomsToTrack
                )
                VerticalCalendar(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .semantics { contentDescription = "Calendar" },
                    contentPadding = PaddingValues(bottom = 48.dp),
                    state = state,
                    monthHeader = { month ->
                        MonthHeader(month)
                    },
                    dayContent = { day ->
                        Day(
                            day = day,
                            calendarDayUIState = calendarUIState.days[day.date],
                            activeSymptom = selectedSymptom
                        ) {
                            navController.navigate(
                                route = "%s/%s/%s"
                                    .format(
                                        Screen.Calendar,
                                        Screen.Log,
                                        day.date.toString()
                                    )
                            )
                        }
                    }
                )
            }
        }
    }
}

// Creates the days
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(
    day: CalendarDay,
    calendarDayUIState: CalendarDayUIState?,
    activeSymptom: Symptom,
    onClick: (CalendarDay) -> Unit,
) {
    val (dayColor, iconId) = getDayColorAndIcon(day, activeSymptom, calendarDayUIState)
    Box(
        modifier = Modifier
            .padding(1.dp)
            .aspectRatio(1f)
    )
    {
        if (day.position == DayPosition.MonthDate) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .fillMaxSize()
                    .background(dayColor)
                    .semantics { contentDescription = day.date.toString() }
                    .border(
                        color = Color(200, 205, 205),
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        enabled = !day.date.isAfter(LocalDate.now()),
                        onClick = {
                            if (!day.date.isAfter(LocalDate.now()))
                                onClick(day)
                        }
                    ),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    text = day.date.dayOfMonth.toString(),
                    color = if (day.date.isAfter(LocalDate.now())) {
                        Color(190, 190, 190)
                    } else {
                        Color.Black
                    }
                )

                Box(modifier = Modifier.padding(12.dp)
                    .align(Alignment.Center)) {
                    if (calendarDayUIState != null) {
                        Image(
                            painterResource(id = iconId),
                            modifier = Modifier
                                .size(20.dp)
                                .offset(y = 2.dp),
                            contentDescription = "DateFlowIcon"
                        )

                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthHeader(calendarMonth: CalendarMonth) {
    // The header that is displayed for every month; contains week & month.
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    Column(
        modifier = Modifier
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Month
        Text(
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            text = calendarMonth.yearMonth.displayText()
        )

        // Days of Week
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = 10.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                )
            }
        }
    }
}


// Function to display Month with Year
@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

// Function to display Month
@RequiresApi(Build.VERSION_CODES.O)
fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}


//@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun CalendarScreenPreview() {
    ThePeriodPurseTheme {
        CalendarScreen(rememberNavController(), viewModel())
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val tabs = listOf(
        CalendarTabItem.CalendarTab,
        CalendarTabItem.CycleTab,
    )
    val pagerState = rememberPagerState()
    Tabs(tabs = tabs, pagerState = pagerState)
}

@Preview
@Composable
fun DisplaySymptomTabPreview() {
    SymptomTab(trackedSymptoms = previewTrackedSymptoms, Symptom.FLOW, {})
}
