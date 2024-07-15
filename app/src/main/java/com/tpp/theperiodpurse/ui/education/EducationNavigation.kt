package com.tpp.theperiodpurse.ui.education

import android.content.Context
import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.component.Background
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

enum class EducationNavigation {
    Learn, DYK, ProductInfo
}

@Composable
fun EducationScreen(
    appViewModel: AppViewModel,
    outController: NavHostController = rememberNavController(),
    navController: NavHostController = rememberNavController(),
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = EducationNavigation.Learn.name,
    ) {
        composable(route = EducationNavigation.Learn.name) {
            Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                Background()
                EducationScreenLayout(appViewModel = appViewModel, outController, navController)
            }
        }
        composable(EducationNavigation.DYK.name) {
            Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                Background()
                EducationDYKScreen(navController, appViewModel = appViewModel)
            }
        }
        composable(
            route = EducationNavigation.ProductInfo.name,
            arguments = listOf(navArgument("elementId") { nullable = true }),
        ) {
            val elementId =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("elementId")
            if (elementId != null) {
                Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                    Background()
                    EducationInfoScreen(appViewModel = appViewModel, navController, elementId)
                }
            }
        }
    }
}
