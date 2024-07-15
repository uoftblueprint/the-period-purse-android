package com.tpp.theperiodpurse.ui.calendar

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.*
import com.kizitonwose.calendar.core.*
import com.tpp.theperiodpurse.ui.calendar.components.*
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.CalendarViewModel
import java.util.*

val tabModifier = Modifier
    .background(Color.White)
    .fillMaxWidth()

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    calendarViewModel: CalendarViewModel,
    appViewModel: AppViewModel,
) {
    // Main calendar screen which allows navigation to cycle page and calendar
    // By default, opens on to the Calendar
    val tabs = listOf(
        CalendarTabItem.CalendarTab,
        CalendarTabItem.CycleTab,
    )
    val pagerState = rememberPagerState()
    Scaffold(
        topBar = {},
        modifier = Modifier.safeDrawingPadding()
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(padding),
        ) {
            Tabs(tabs = tabs, pagerState = pagerState, appViewModel = appViewModel)
            TabsContent(
                tabs = tabs,
                pagerState = pagerState,
                calendarViewModel = calendarViewModel,
                navController = navController,
                appViewModel = appViewModel,
            )
        }
    }
}

@Preview
@Composable
fun CalendarScreenPreview() {
    CalendarScreen(rememberNavController(), viewModel(), viewModel())
}
