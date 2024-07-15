package com.tpp.theperiodpurse

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.tpp.theperiodpurse.ui.component.BottomNavigation
import com.tpp.theperiodpurse.ui.component.FloatingActionButton
import com.tpp.theperiodpurse.ui.component.LoadingScreen
import com.tpp.theperiodpurse.ui.onboarding.*
import com.tpp.theperiodpurse.ui.symptomlog.LoggingOptionsPopup
import com.tpp.theperiodpurse.ui.theme.ThePeriodPurseTheme
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.CalendarViewModel
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.Black.toArgb(),
                Color.Black.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.Black.toArgb(),
                Color.Black.toArgb()
            )
        )

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE_APPDATA))
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            val context = LocalContext.current.applicationContext
            var hasNotificationPermission by remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            POST_NOTIFICATIONS,
                        ) == PackageManager.PERMISSION_GRANTED,
                    )
                } else {
                    mutableStateOf(true)
                }
            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasNotificationPermission = isGranted
                    if (!isGranted) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)
                        }
                    }
                },
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                SideEffect {
                    launcher.launch(POST_NOTIFICATIONS)
                }
            }
            Application(context = context, signIn = { signInLauncher -> signIn(signInLauncher) },
                signout = { signOut() })
        }
    }
    private fun signIn(launcher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private fun signOut() {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            googleSignInClient.signOut().addOnCompleteListener {
                Toast.makeText(this, "SignOut Successful", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Application(
    context: Context,
    signIn: (launcher: ActivityResultLauncher<Intent>) -> Unit,
    signout: () -> Unit = {},
    hasNotificationsPermission: Boolean = false,
) {
    AppScreen(
        signIn = { signInLauncher -> signIn(signInLauncher) },
        context = context,
        signout = signout,
        hasNotificationsPermissions = hasNotificationsPermission,
    )
    createNotificationChannel(context)
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "notification_channel",
            "Notification",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        channel.description = "Used for the reminder notifications"

        val notificationManager =
            context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = viewModel(),
    onboardViewModel: OnboardViewModel = viewModel(),
    calendarViewModel: CalendarViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    skipWelcome: Boolean = false,
    skipDatabase: Boolean = false,
    skipOnboarding: Boolean = false,
    context: Context,
    signout: () -> Unit = {},
    signIn: (launcher: ActivityResultLauncher<Intent>) -> Unit,
    hasNotificationsPermissions: Boolean = false,
) {
    ThePeriodPurseTheme(
        appViewModel,
    ) {
        var loggingOptionsVisible by remember { mutableStateOf(false) }
        var skipOnboarding = skipOnboarding
        val isOnboarded by onboardViewModel.isOnboarded.observeAsState(initial = null)

        if (!skipDatabase) {
            LaunchedEffect(Unit) {
                onboardViewModel.checkOnboardedStatus(context)
            }
        }
        val startdestination: String

        if (isOnboarded == null && !skipDatabase) {
            LoadingScreen(appViewModel)
        } else {
            if (!skipDatabase) {
                skipOnboarding = (isOnboarded as Boolean)
            }
            if (skipOnboarding) {
                startdestination = OnboardingScreen.LoadDatabase.name
            } else if (skipWelcome) {
                startdestination = OnboardingScreen.QuestionOne.name
            } else {
                startdestination = OnboardingScreen.Welcome.name
            }
            Scaffold(
                floatingActionButton = {
                    if (currentRoute(navController) in screensWithNavigationBar) {
                        FloatingActionButton(
                            navController = navController,
                            onClickInCalendar = { loggingOptionsVisible = true },
                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
            ) { innerPadding ->
                Box (
                    modifier = Modifier
                        .padding(innerPadding)
                ){
                    NavigationGraph(
                        navController = navController,
                        startDestination = startdestination,
                        onboardViewModel = onboardViewModel,
                        appViewModel = appViewModel,
                        calendarViewModel = calendarViewModel,
                        context = context,
                        signout = signout,
                        signIn = { signInLauncher -> signIn(signInLauncher) }
                    )

                    if (loggingOptionsVisible) {
                        Log.d("AppScreen", "Rendering logging options")
                        LoggingOptionsPopup(
                            onLogDailySymptomsClick = {
                                navigateToLogScreenWithDate(
                                    LocalDate.now(),
                                    navController,
                                )
                            },
                            onLogMultiplePeriodDates = { navController.navigate(Screen.LogMultipleDates.name) },
                            onExit = { loggingOptionsVisible = false },
                            modifier = modifier.padding(bottom = 64.dp),
                            appViewModel = appViewModel
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (currentRoute(navController) in screensWithNavigationBar) {
                        BottomNavigation(navController = navController, appViewModel = appViewModel)
                    }
                }
            }
        }
    }
}
