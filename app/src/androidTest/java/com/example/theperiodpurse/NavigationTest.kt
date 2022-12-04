package com.example.theperiodpurse

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            navController =
                TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )
            ScreenApp(navController = navController)
        }
    }

    private fun skipAllOnboardingScreens() {
        composeTestRule.onNodeWithText("Quick Start").performClick()
        for (i in 1..3) {
            composeTestRule.onNodeWithText("Skip").performClick()
        }
        composeTestRule.onNodeWithText("Let's go!").performClick()
    }

    @Test
    fun appNavHost_clickSettings_navigatesToSettingsScreen() {
        skipAllOnboardingScreens()
        composeTestRule.onNodeWithText(Screen.Settings.name)
            .performClick()
        navController.assertCurrentRouteName(Screen.Settings.name)
    }

    private fun navigateToSettingsScreen() {
        composeTestRule.onNodeWithText(Screen.Settings.name).performClick()
    }

    @Test
    fun appNavHost_clickSettings_navigatesToInfoScreen() {
        skipAllOnboardingScreens()
        composeTestRule.onNodeWithText(Screen.Learn.name)
            .performClick()
        navController.assertCurrentRouteName(Screen.Learn.name)
    }

    private fun navigateToInfoScreen() {
        composeTestRule.onNodeWithText(Screen.Learn.name).performClick()
    }

    @Test
    fun appNavHost_clickCalendarFABOnSettingsScreen_navigatesToCalendarScreen() {
        skipAllOnboardingScreens()
        navigateToSettingsScreen()
        composeTestRule.onNodeWithContentDescription("Navigate to Calendar page")
            .performClick()
        navController.assertCurrentRouteName(Screen.Calendar.name)
    }

    @Test
    fun appNavHost_clickCalendarFABOnInfoScreen_navigatesToCalendarScreen() {
        skipAllOnboardingScreens()
        navigateToInfoScreen()
        composeTestRule.onNodeWithContentDescription("Navigate to Calendar page")
            .performClick()
        navController.assertCurrentRouteName(Screen.Calendar.name)
    }

}
