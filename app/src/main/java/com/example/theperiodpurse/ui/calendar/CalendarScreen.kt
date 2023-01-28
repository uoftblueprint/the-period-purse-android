package com.example.theperiodpurse.ui.calendar


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.theperiodpurse.R
import com.example.theperiodpurse.Screen
import com.example.theperiodpurse.data.Symptom
import com.example.theperiodpurse.ui.theme.ThePeriodPurseTheme
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

@Composable
fun SymptomTab(modifier: Modifier = Modifier, trackedSymptoms: List<Symptom>) {
    var expanded by remember { mutableStateOf(false) }
    var activeSymptom: Symptom? by remember { mutableStateOf(null) }
    Column(modifier = modifier) {
        DisplaySymptomTab(
            activeSymptom = activeSymptom,
            expanded = expanded,
            onExpandButtonClick = { expanded = !expanded },
            modifier = tabModifier
        )
        if (expanded) {
            SwitchSymptomTab(
                activeSymptom = activeSymptom,
                symptoms = trackedSymptoms,
                onSymptomOnClicks = trackedSymptoms.map { { activeSymptom = it } },
                modifier = tabModifier
            )
        }
    }
}

@Composable
private fun DisplaySymptomTab(
    activeSymptom: Symptom?,
    expanded: Boolean,
    onExpandButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = stringResource(activeSymptom?.nameId ?: Symptom.FLOW.nameId),
            modifier = Modifier.padding(end = 2.dp)
        )
        Icon(
            painter = painterResource(
                id = activeSymptom?.resourceId ?: Symptom.FLOW.resourceId
            ),
            tint = Color.Black,
            contentDescription = activeSymptom?.name,
            modifier = Modifier.padding(end = 0.dp)
                .testTag("Selected Symptom")
        )
        SwitchSymptomButton(
            expanded = expanded,
            onClick = onExpandButtonClick
        )
    }
}

@Composable
private fun SwitchSymptomButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            tint = Color.Gray,
            contentDescription = stringResource(R.string.expand_button_symptoms_content_description)
        )
    }
}

@Composable
private fun SwitchSymptomTab(
    activeSymptom: Symptom?,
    symptoms: List<Symptom>,
    onSymptomOnClicks: List<() -> Unit>,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.testTag("Symptom Options")
    ) {
        symptoms.zip(onSymptomOnClicks).forEach { (symptom, onClick) ->
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            IconButton(
                onClick = onClick,
                interactionSource = interactionSource
            ) {
                val defaultColor = Color.Black
                val color = if (isPressed) {
                    defaultColor.copy(ContentAlpha.disabled)
                } else if (activeSymptom == symptom) {
                    Color(0xFFBF3428)
                } else {
                    defaultColor
                }
                Icon(
                    painter = painterResource(id = symptom.resourceId),
                    tint = color,
                    contentDescription = stringResource(symptom.nameId)
                )
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<CalendarTabItem>, pagerState: PagerState) {
    // Composable for tabs
    // Tabs are displayed in ordered row by tabs list
    // PagerState allows for navigation/animation between paginated layouts
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.LightGray,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(
                    pagerState,
                    tabPositions
                ),
                color = Color(0xff5a9f93)
            )
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(tabs: List<CalendarTabItem>, pagerState: PagerState, navController: NavController) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen(navController)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarScreen(navController: NavController) {
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
                TabsContent(tabs = tabs, pagerState = pagerState, navController = navController)
            }
        }
    }
}

val previewTrackedSymptoms = Symptom.values().asList()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreenLayout(navController: NavController) {
    // Contains the swappable content
    ThePeriodPurseTheme {
        val bg = painterResource(R.drawable.colourwatercolour)

        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
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
            Image(painter = bg,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = "Calendar Page" },
                contentScale = ContentScale.FillBounds,
            )
            Column {
                SymptomTab(
                    trackedSymptoms = previewTrackedSymptoms
//                trackedSymptoms = userDAO.get().symptomsToTrack
                )
                VerticalCalendar(
                    modifier = Modifier.semantics { contentDescription = "Calendar" },
                    state = state,
                    monthHeader = { month ->
                        MonthHeader(month) },
                    dayContent = { day ->
                        Day(day, isSelected = selectedDate == day.date) { date ->
                            selectedDate = if (selectedDate == date.date) null
                            else date.date
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
fun Day(day: CalendarDay,
        isSelected: Boolean,
        onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(1.dp)
            .aspectRatio(1f),
        )
    {
        if (day.position == DayPosition.MonthDate) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(shape = RoundedCornerShape(6.dp))
                    .fillMaxSize()
                    .background(color = if (day.date.isAfter(LocalDate.now())) Color.LightGray
                    else Color.White)
                    .semantics { contentDescription = day.date.toString() }
                    .border(
                        color = Color.Gray,
                        width = 1.dp,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable(
                        enabled = !day.date.isAfter(LocalDate.now()),
                        onClick = { if (!day.date.isAfter(LocalDate.now()))
                            onClick(day) }
                    ),
            ) {
                Text(modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    fontSize = 14.sp,
                    text = day.date.dayOfMonth.toString()
                )
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
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        // Month
        Text(
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            text = calendarMonth.yearMonth.displayText()
        )

        // Days of Week
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
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
        CalendarScreen(rememberNavController())
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
    SymptomTab(trackedSymptoms = previewTrackedSymptoms)
}
