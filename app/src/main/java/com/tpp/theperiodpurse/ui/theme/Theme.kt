package com.tpp.theperiodpurse.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
     */
)

@Composable
fun ThePeriodPurseTheme(
    appViewModel: AppViewModel,
    content: @Composable () -> Unit,
) {
    val colors = LightColorPalette

    val systemUiController = rememberSystemUiController()
    val darkMode = appViewModel.uiState.collectAsState().value.darkMode
    LaunchedEffect(darkMode) {
        if (darkMode) {
            systemUiController.setNavigationBarColor(appViewModel.colorPalette.BottomBarColor1,
                false)
        } else {
            systemUiController.setNavigationBarColor(appViewModel.colorPalette.BottomBarColor1,
                true)
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
