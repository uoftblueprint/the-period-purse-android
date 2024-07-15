package com.tpp.theperiodpurse.ui.setting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.component.Background
import com.tpp.theperiodpurse.ui.state.AppUiState
import com.tpp.theperiodpurse.ui.state.CalendarUIState
import com.tpp.theperiodpurse.ui.state.OnboardUIState
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel

enum class SettingScreenNavigation(@StringRes val title: Int) {
    Start(title = R.string.settings_home), Notification(title = R.string.customize_notifications), BackUpAccount(
        title = R.string.back_up_account,
    ),
    DeleteAccount(title = R.string.delete_account),
    ResetDatabase(title = R.string.reset_database),
    BackupDatabase(title = R.string.back_up_database),
    ConfirmBackup(title = R.string.confirm_back_up),
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun SettingAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
) {
    TopAppBar(
        title = { Text(text = currentScreen, color = appViewModel.colorPalette.MainFontColor) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = appViewModel.colorPalette.MainFontColor
                    )
                }
            }
        },
        backgroundColor = appViewModel.colorPalette.HeaderColor1,
        elevation = 0.dp,
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    outController: NavHostController = rememberNavController(),
    navController: NavHostController = rememberNavController(),
    context: Context,
    appViewModel: AppViewModel?,
    onboardUiState: OnboardUIState?,
    onboardViewModel: OnboardViewModel?,
    appUiState: AppUiState?,
    calUiState: CalendarUIState?,
    signIn: (launcher: ActivityResultLauncher<Intent>) -> Unit,
    signout: () -> Unit = {},
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SettingScreenNavigation.valueOf(
        backStackEntry?.destination?.route ?: SettingScreenNavigation.Start.name,
    )

    Scaffold(
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SettingScreenNavigation.Start.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = SettingScreenNavigation.Start.name) {
                if (appViewModel != null) {
                    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                        Background()
                        SettingScreenLayout(
                            outController = outController,
                            onNotificationClicked = {
                                navController.navigate(SettingScreenNavigation.Notification.name)
                            },
                            onBackUpClicked = {
                                navController.navigate(SettingScreenNavigation.BackUpAccount.name)
                            },
                            onDeleteClicked = {
                                navController.navigate(SettingScreenNavigation.DeleteAccount.name)
                            },
                            appViewModel = appViewModel,
                            context = context,
                            navController = navController
                        )
                    }
                }
            }
            composable(route = SettingScreenNavigation.Notification.name) {
                val hasNotificationPermission by remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS,
                            ) == PackageManager.PERMISSION_GRANTED,
                        )
                    } else {
                        mutableStateOf(true)
                    }
                }
                if (appViewModel != null) {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        Background()
                        SettingAppBar(
                            currentScreen = currentScreen.name,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            appViewModel = appViewModel
                        )
                        NotificationsLayout(
                            context = context,
                            hasNotificationPermission,
                            appViewModel,
                        )
                    }
                }
            }
            composable(route = SettingScreenNavigation.BackUpAccount.name) {
                if (appViewModel != null) {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        Background()
                        SettingAppBar(
                            currentScreen = "Back Up Account",
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            appViewModel = appViewModel
                        )
                        BackUpAccountScreen(
                            navController = navController,
                            signIn = { signInLauncher -> signIn(signInLauncher) },
                            signOut = signout,
                            context = context,
                            appViewModel = appViewModel
                        )
                    }
                }
            }
            composable(route = SettingScreenNavigation.DeleteAccount.name) {
                if (appViewModel != null) {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        Background()
                        SettingAppBar(
                            currentScreen = "Delete Account",
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            appViewModel = appViewModel
                        )
                        DeleteAccountScreen(
                            navController = navController,
                            appViewModel = appViewModel
                        )
                    }
                }
            }
            composable(route = SettingScreenNavigation.ResetDatabase.name) {
                if (onboardViewModel != null && onboardUiState != null && appUiState != null && calUiState != null && appViewModel != null) {
                    ResetDatabase(
                        context = context,
                        onboardViewModel = onboardViewModel,
                        appViewModel= appViewModel,
                        outController = outController,
                        onboardUiState = onboardUiState,
                        appUiState = appUiState,
                        calUiState = calUiState,
                        signout = signout,
                    )
                }
            }
            composable(route = SettingScreenNavigation.BackupDatabase.name) {
                if (onboardViewModel != null && onboardUiState != null) {
                    if (appViewModel != null) {
                        BackupDatabase(
                            onboardViewModel = onboardViewModel,
                            appViewModel= appViewModel,
                            navController = navController,
                            signout = signout,
                            context = context,
                        )
                    }
                }
            }
            composable(route = SettingScreenNavigation.ConfirmBackup.name) {
                ConfirmBackUp(navController = navController)
            }
        }
    }
}
