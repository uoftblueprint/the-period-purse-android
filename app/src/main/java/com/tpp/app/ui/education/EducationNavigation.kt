package com.tpp.app.ui.education

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tpp.app.R
import com.tpp.app.ui.viewmodel.AppViewModel

enum class EducationNavigation {
    Learn, DYK, ProductInfo
}

@Composable
fun EducationScreen(
    appViewModel: AppViewModel,
    outController: NavHostController = rememberNavController(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = EducationNavigation.Learn.name,
    ) {
        composable(route = EducationNavigation.Learn.name) {
            EducationScreenLayout(appViewModel = appViewModel, outController, navController)
        }
        composable(EducationNavigation.DYK.name) {
            EducationDYKScreen(navController, appViewModel = appViewModel)
        }
        composable(
            route = EducationNavigation.ProductInfo.name,
            arguments = listOf(navArgument("elementId") { nullable = true }),
        ) {
            val elementId =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("elementId")
            if (elementId != null) {
                EducationInfoScreen(appViewModel = appViewModel, navController, elementId)
            }
        }
    }
}

@Composable
fun EducationBackground(appViewModel: AppViewModel) {
    Image(
        painter = painterResource(appViewModel.colorPalette.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
    )
}
