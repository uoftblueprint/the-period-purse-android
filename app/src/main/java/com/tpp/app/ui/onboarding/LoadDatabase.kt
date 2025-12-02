package com.theperiodpurse.app.ui.onboarding

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.theperiodpurse.app.Screen
import com.theperiodpurse.app.ui.component.LoadingScreen
import com.theperiodpurse.app.ui.viewmodel.AppViewModel
import com.theperiodpurse.app.ui.viewmodel.CalendarViewModel

// TODO: add a clean up function for all view models so that they can be re-used if the user
//  deletes account
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoadDatabase(
    navController: NavHostController,
    appViewModel: AppViewModel,
    calViewModel: CalendarViewModel,
    context: Context,
) {
    Log.d("Load Database", "Navigating to calendar")
    val databaseIsLoadedFromStorage by appViewModel.databaseIsLoadedFromStorage.observeAsState(initial = null)
//    Log.d("Load Database", databaseIsLoadedFromStorage.toString())
    LaunchedEffect(Unit) {
        Log.d("Load Database", "Loading database")
        appViewModel.loadData(calViewModel, context)
    }
    if (databaseIsLoadedFromStorage == null) {
        LoadingScreen(appViewModel)
    } else {
        LaunchedEffect(Unit) {
            Log.d("Load Database", "Navigating to calendar")
            navController.navigate(Screen.Calendar.name)
            appViewModel.databaseIsLoadedFromStorage.postValue(null)
        }
    }
}
