package com.example.theperiodpurse

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.theperiodpurse.ui.component.BottomNavigation
import com.example.theperiodpurse.ui.component.FloatingActionButton
import com.example.theperiodpurse.ui.onboarding.*
import com.example.theperiodpurse.ui.theme.ThePeriodPurseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThePeriodPurseTheme {
                Application()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Application() {
    ScreenApp()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenApp(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = viewModel(),
    onboardViewModel: OnboardViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    appViewModel.loadData()
    Scaffold(
        bottomBar = {
            if (currentRoute(navController) in Screen.values().map{ it.name }) {
                BottomNavigation(navController = navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                navController = navController
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { innerPadding ->
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        NavigationGraph(
            navController = navController,
            startDestination =
                if (appViewModel.uiState.collectAsState().value.trackedSymptoms.isNotEmpty()) Screen.Calendar.name
                else OnboardingScreen.Welcome.name,
            onboardViewModel,
            appViewModel,
            modifier = modifier.padding(innerPadding)
        )
    }
}
