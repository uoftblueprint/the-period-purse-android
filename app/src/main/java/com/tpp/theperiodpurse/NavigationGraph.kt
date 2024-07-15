package com.tpp.theperiodpurse

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
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
import com.tpp.theperiodpurse.ui.calendar.CalendarScreen
import com.tpp.theperiodpurse.ui.cycle.CycleScreenLayout
import com.tpp.theperiodpurse.ui.cycle.PeriodHistoryLayout
import com.tpp.theperiodpurse.ui.education.*
import com.tpp.theperiodpurse.ui.legal.PrivacyScreen
import com.tpp.theperiodpurse.ui.legal.TermsScreen
import com.tpp.theperiodpurse.ui.onboarding.*
import com.tpp.theperiodpurse.ui.onboarding.SummaryScreen
import com.tpp.theperiodpurse.ui.setting.SettingsScreen
import com.tpp.theperiodpurse.ui.symptomlog.LogMultipleDatesScreen
import com.tpp.theperiodpurse.ui.symptomlog.LogScreen
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.CalendarViewModel
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel
import java.time.LocalDate

enum class Screen {
    Calendar,
    Log,
    Cycle,
    Settings,
    Learn,
    LogMultipleDates,
    CycleFullHistory,
}

enum class LegalScreen {
    Terms,
    Privacy,
}

val screensWithNavigationBar = arrayOf(
    Screen.Calendar.name,
    Screen.Log.name,
    Screen.Cycle.name,
    Screen.Settings.name,
    Screen.Learn.name,
    Screen.CycleFullHistory.name,
)

enum class OnboardingScreen {
    Welcome,
    QuestionOne,
    QuestionTwo,
    QuestionThree,
    Summary,
    RestoreFromGoogleDrivePrompt,
    LoadDatabase,
    DownloadBackupFromGoogleDrive,
    DateRangePicker,
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
    signout: () -> Unit = {},
    signIn: (launcher: ActivityResultLauncher<Intent>) -> Unit,
) {
    val onboardUIState by onboardViewModel.uiState.collectAsState()
    val appUiState by appViewModel.uiState.collectAsState()
    val calUiState by calendarViewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.Calendar.name) {
            Log.d("NavGraph", "Someone navigated to Screen Calendar")
            CalendarScreen(
                navController = navController,
                appViewModel = appViewModel,
                calendarViewModel = calendarViewModel,
            )
        }

        composable(
            route = "%s/%s/{date}"
                .format(Screen.Calendar, Screen.Log),
            arguments = listOf(navArgument("date") { type = NavType.StringType }),
        ) { backStackEntry ->
            // date is in yyyy-mm-dd format
            val date = backStackEntry.arguments?.getString("date")
            if (date != null) {
                LogScreen(
                    date = date,
                    navController = navController,
                    appViewModel = appViewModel,
                    calendarViewModel = calendarViewModel,
                    context = context,
                )
            }
        }

        composable(route = Screen.LogMultipleDates.name) {
            LogMultipleDatesScreen(
                onClose = { navController.navigateUp() },
                calendarViewModel,
                appViewModel,
            )
        }

        composable(route = Screen.Settings.name) {
            SettingsScreen(
                appViewModel = appViewModel,
                outController = navController,
                context = context,
                onboardUiState = onboardUIState,
                onboardViewModel = onboardViewModel,
                appUiState = appUiState,
                calUiState = calUiState,
                signIn = { signInLauncher -> signIn(signInLauncher) },
                signout = signout,
            )
        }

        composable(route = Screen.Cycle.name) {
            CycleScreenLayout(
                appViewModel = appViewModel,
                navController = navController,
            )
        }

        composable(route = Screen.CycleFullHistory.name) {
            PeriodHistoryLayout(
                appViewModel = appViewModel,
                navController = navController,
            )
        }

        // Education Screens

        composable(route = Screen.Learn.name) {
            EducationScreen(outController = navController, appViewModel = appViewModel, context=context)
        }

        composable(LegalScreen.Terms.name) {
            TermsScreen(appViewModel = appViewModel, navController = navController)
        }

        composable(LegalScreen.Privacy.name) {
            PrivacyScreen(navController, appViewModel)
        }

        // Welcome Screen
        composable(route = OnboardingScreen.Welcome.name) {
            WelcomeScreen(
                onNextButtonClicked =
                { navController.navigate(OnboardingScreen.QuestionOne.name) },
                signIn = { signInLauncher -> signIn(signInLauncher) },
                signout = signout,
                navController = navController,
                context = context,
                onboardUIState = onboardUIState,
                appViewModel = appViewModel,
            )
        }

        // Onboard Screens
        composable(route = OnboardingScreen.QuestionOne.name) {
            QuestionOneScreen(
                navController = navController,
                onSelectionChanged = { onboardViewModel.setQuantity(it.toInt()) },
                canNavigateBack = navController.previousBackStackEntry != null,
                onboardUiState = onboardUIState,
                viewModel = onboardViewModel,
                signOut = signout,
                context = context,
            )
        }
        composable(route = OnboardingScreen.QuestionTwo.name) {
            QuestionTwoScreen(
                navController = navController,
                onboardUiState = onboardUIState,
                onSelectionChanged = { onboardViewModel.setDate(it) },
                navigateUp = { navController.navigate(OnboardingScreen.QuestionOne.name) },
                canNavigateBack = navController.previousBackStackEntry != null,
            )
        }

        composable(route = OnboardingScreen.QuestionThree.name) {
            QuestionThreeScreen(
                navController = navController,
                onboardUiState = onboardUIState,
                onSelectionChanged = { onboardViewModel.setSymptoms(it) },
                canNavigateBack = navController.previousBackStackEntry != null,
            )
        }
        composable(route = OnboardingScreen.Summary.name) {
            SummaryScreen(
                onboardUiState = onboardUIState,
                onSendButtonClicked = {
                    Log.d("Summary Screen", "Navigating to Load database")
                    navController.navigate(OnboardingScreen.LoadDatabase.name)
                },
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null,
                viewModel = onboardViewModel,
                appViewModel = appViewModel,
                calendarViewModel = calendarViewModel,
//                onCancelButtonClicked = {
//                    cancelOrderAndNavigateToStart(onboardViewModel, navController)
//                },
            )
        }
        composable(route = OnboardingScreen.LoadDatabase.name) {
            LoadDatabase(
                appViewModel = appViewModel,
                calViewModel = calendarViewModel,
                navController = navController,
                context = context,
            )
        }
        composable(route = OnboardingScreen.RestoreFromGoogleDrivePrompt.name) {
            RestoreFromGoogleDrivePrompt(
                onboardViewModel = onboardViewModel,
                appViewModel = appViewModel,
                navHostController = navController,
                context = context,
                signout = signout,
                googleAccount = onboardUIState.googleAccount,
            )
        }

        composable(route = OnboardingScreen.DownloadBackupFromGoogleDrive.name) {
            DownloadBackupFromGoogleDrive(
                onboardViewModel = onboardViewModel,
                appViewModel = appViewModel,
                navHostController = navController,
                signout = signout,
            )
        }

        composable(route = OnboardingScreen.DateRangePicker.name) {
            DateRangePicker(
                { navController.navigate(OnboardingScreen.QuestionTwo.name) },
                onboardViewModel,
                onboardUIState,
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

fun navigateToLogScreenWithDate(date: LocalDate, navController: NavController) {
    navController.navigate(
        route = "%s/%s/%s"
            .format(Screen.Calendar, Screen.Log, date.toString()),
    )
}
