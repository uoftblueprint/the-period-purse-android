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
import com.tpp.theperiodpurse.ui.symptomlog.LogMultipleDatesScreen
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.ui.SummaryScreen
import com.tpp.theperiodpurse.ui.calendar.CalendarScreen
import com.tpp.theperiodpurse.ui.calendar.CalendarViewModel
import com.tpp.theperiodpurse.ui.cycle.CycleScreenLayout
import com.tpp.theperiodpurse.ui.education.*
import com.tpp.theperiodpurse.ui.legal.PrivacyScreen
import com.tpp.theperiodpurse.ui.legal.TermsScreen
import com.tpp.theperiodpurse.ui.onboarding.*
import com.tpp.theperiodpurse.ui.setting.LoadDatabase
import com.tpp.theperiodpurse.ui.setting.SettingsScreen
import com.tpp.theperiodpurse.ui.symptomlog.LogScreen
import java.time.LocalDate

enum class Screen {
    Calendar,
    Log,
    Cycle,
    Settings,
    Learn,
    LogMultipleDates
}

enum class LegalScreen {
    Terms,
    Privacy
}

val screensWithNavigationBar = arrayOf(
    Screen.Calendar.name, Screen.Log.name, Screen.Cycle.name,
    Screen.Settings.name, Screen.Learn.name
)

enum class OnboardingScreen {
    Welcome,
    QuestionOne,
    QuestionTwo,
    QuestionThree,
    Summary,
    LoadDatabase,
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    calendarViewModel: CalendarViewModel,
    onboardViewModel: OnboardViewModel,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier,
    context: Context,
    signIn: () -> Unit
) {
    val onboardUIState by onboardViewModel.uiState.collectAsState()
    val appUiState by appViewModel.uiState.collectAsState()
    val calUiState by calendarViewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Calendar.name) {
            CalendarScreen(
                navController = navController,
                appViewModel = appViewModel,
                calendarViewModel = calendarViewModel
            )
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
                    appViewModel = appViewModel,
                    calendarViewModel = calendarViewModel
                )
            }
        }

        composable(route = Screen.LogMultipleDates.name) {
            LogMultipleDatesScreen(
                onClose = { navController.navigateUp() },
                calendarViewModel
            )
        }

        composable(route = Screen.Settings.name) {
            SettingsScreen(appViewModel = appViewModel,
                outController = navController,
                context = context,
                onboardUiState = onboardUIState,
                onboardViewModel = onboardViewModel,
                appUiState = appUiState,
                calUiState = calUiState)
        }

        composable(route = Screen.Cycle.name) {
            CycleScreenLayout(
                appViewModel = appViewModel,
            )
        }

        composable(route = Screen.Learn.name) {
            EducationScreen(outController = navController)
        }

        composable(LegalScreen.Terms.name) {
            TermsScreen(navController)
        }

        composable(LegalScreen.Privacy.name) {
            PrivacyScreen(navController)
        }


        // Welcome Screen
        composable(route = OnboardingScreen.Welcome.name) {
            WelcomeScreen(
                navController = navController,
                onNextButtonClicked =
                { navController.navigate(OnboardingScreen.QuestionOne.name) },
                signIn = signIn
            )
        }


        // Onboard Screens
        composable(route = OnboardingScreen.QuestionOne.name) {
            QuestionOneScreen(
                navController = navController,
                onSelectionChanged = { onboardViewModel.setQuantity(it.toInt()) },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null,
                onboardUiState = onboardUIState,
            )
        }
        composable(route = OnboardingScreen.QuestionTwo.name) {
            QuestionTwoScreen(
                navController = navController,
                onboardUiState = onboardUIState,
                onSelectionChanged = { onboardViewModel.setDate(it) },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null
            )
        }

        composable(route = OnboardingScreen.QuestionThree.name) {
            QuestionThreeScreen(
                navController = navController,
                onboardUiState = onboardUIState,
                onSelectionChanged = { onboardViewModel.setSymptoms(it) },
                canNavigateBack = navController.previousBackStackEntry != null
            )
        }
        composable(route = OnboardingScreen.Summary.name) {
            SummaryScreen(
                onboardUiState = onboardUIState,
                onSendButtonClicked = {
                    navController.navigate(OnboardingScreen.LoadDatabase.name)

                },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null,
                viewModel = onboardViewModel
//                onCancelButtonClicked = {
//                    cancelOrderAndNavigateToStart(onboardViewModel, navController)
//                },
            )
        }
        composable(route = OnboardingScreen.LoadDatabase.name) {
            LoadDatabase(
                appViewModel = appViewModel,
                calViewModel = calendarViewModel,
                navController = navController
            )
        }
    }
}

/**
 * Resets the [OnboardUIState] and pops up to [OnboardingScreen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: OnboardViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(OnboardingScreen.Welcome.name, inclusive = false)
}

fun navigateToLogScreenWithDate(date: LocalDate, navController: NavController) {
    navController.navigate(route = "%s/%s/%s"
        .format(Screen.Calendar, Screen.Log, date.toString()))
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
