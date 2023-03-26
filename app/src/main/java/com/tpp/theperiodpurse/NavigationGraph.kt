package com.tpp.theperiodpurse

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.ui.SummaryScreen
import com.tpp.theperiodpurse.ui.calendar.CalendarScreen
import com.tpp.theperiodpurse.ui.calendar.CalendarViewModel
import com.tpp.theperiodpurse.ui.cycle.CycleScreenLayout
import com.tpp.theperiodpurse.ui.education.*
import com.tpp.theperiodpurse.ui.onboarding.*
import com.tpp.theperiodpurse.ui.setting.SettingsScreen
import com.tpp.theperiodpurse.ui.symptomlog.LogScreen

enum class Screen() {
    Calendar,
    Log,
    Cycle,
    Settings,
    Learn
}

enum class OnboardingScreen() {
    Welcome,
    QuestionOne,
    QuestionTwo,
    QuestionThree,
    Summary,
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    viewModel: OnboardViewModel,
    calendarViewModel: CalendarViewModel,
    modifier: Modifier = Modifier,
    signIn: () -> Unit,
    context: Context
) {
    val uiState by viewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Calendar.name) {
            CalendarScreen(navController = navController, calendarViewModel)
        }

        composable(
            route = "%s/%s/{date}"
                .format(Screen.Calendar, Screen.Log),
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            // date is in yyyy-mm-dd format
            val date = backStackEntry.arguments?.getString("date")
            if (date != null) {
                LogScreen(
                    date = date,
                    navController = navController,
                    calendarViewModel = calendarViewModel
                )
            }
        }

        composable(route = Screen.Settings.name) {
            SettingsScreen(context = context,
                viewModel = viewModel,
                outController = navController,
                onboardUiState = uiState)
        }

        composable(route = Screen.Cycle.name) {
            CycleScreenLayout()
        }

        // Education Screens

        composable(route = Screen.Learn.name) {
            EducationScreenLayout()
        }



        // Welcome Screen
        composable(route = OnboardingScreen.Welcome.name) {
            WelcomeScreen(
                onNextButtonClicked =
                { navController.navigate(OnboardingScreen.QuestionOne.name) },
                signIn = signIn
            )
        }


        // Onboard Screens
        composable(route = OnboardingScreen.QuestionOne.name) {
            QuestionOneScreen(
                navController = navController,
                onSelectionChanged = { viewModel.setQuantity(it.toInt()) },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null,
                onboardUiState = uiState,
            )
        }
        composable(route = OnboardingScreen.QuestionTwo.name) {
            QuestionTwoScreen(
                navController = navController,
                onboardUiState = uiState,
                onSelectionChanged = { viewModel.setDate(it) },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null
            )
        }

        composable(route = OnboardingScreen.QuestionThree.name) {
            QuestionThreeScreen(
                navController = navController,
                onboardUiState = uiState,
                onSelectionChanged = { viewModel.setSymptoms(it) },
                canNavigateBack = navController.previousBackStackEntry != null
            )
        }
        composable(route = OnboardingScreen.Summary.name) {
            SummaryScreen(
                onboardUiState = uiState,
                onSendButtonClicked = {
                    navController.popBackStack(OnboardingScreen.Welcome.name, inclusive = true)
                    navController.navigate(Screen.Calendar.name)
                },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null,
                viewModel = viewModel
            )
        }
    }
}
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
