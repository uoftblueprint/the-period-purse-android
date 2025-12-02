package com.tpp.app.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.tpp.app.ui.cycle.CycleScreenLayout
import com.tpp.app.ui.viewmodel.AppViewModel
import com.tpp.app.ui.viewmodel.CalendarViewModel

typealias ComposableNavFun = @Composable (calendarViewModel: CalendarViewModel, navController: NavController, appViewModel: AppViewModel) -> Unit

open class CalendarTabItem(var title: String, var screen: ComposableNavFun) {
    // Sealed Class to separately store the tab data from any screens main file
    @RequiresApi(Build.VERSION_CODES.O)
    object CalendarTab : CalendarTabItem(
        "Calendar",
        { calendarViewModel, navController, appViewModel ->
            CalendarScreenLayout(
                calendarViewModel = calendarViewModel,
                navController,
                appViewModel = appViewModel,
            )
        },
    )
    object CycleTab : CalendarTabItem("Cycle", { _, navController, appViewModel ->
        CycleScreenLayout(appViewModel = appViewModel, navController = navController)
    })
}
