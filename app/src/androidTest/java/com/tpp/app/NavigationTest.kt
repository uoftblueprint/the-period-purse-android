package com.tpp.app

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.tpp.app.ui.viewmodel.AppViewModel
import com.tpp.app.ui.viewmodel.CalendarViewModel
import com.tpp.app.ui.viewmodel.OnboardViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltAndroidTest
class NavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Inject
    lateinit var appViewModel: AppViewModel

    @Inject
    lateinit var onboardViewModel: OnboardViewModel

    @Inject
    lateinit var calendarViewModel: CalendarViewModel

    // Used to manage the components' state and is used to perform injection on tests
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setupNavHost() {
        hiltRule.inject()
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator(),
            )
            AppScreen(
                navController = navController,
                skipDatabase = true,
                skipOnboarding = true,
                context = LocalContext.current,
                signIn = { signIn() },
                onboardViewModel = onboardViewModel,
                appViewModel = appViewModel,
                calendarViewModel = calendarViewModel,
            )
        }
    }

    @Test
    fun appNavHost_clickSettings_navigatesToSettingsScreen() {
        composeTestRule.onNodeWithText(Screen.Settings.name).performClick()
        navController.assertCurrentRouteName(Screen.Settings.name)
    }

    private fun navigateToSettingsScreen() {
        composeTestRule.onNodeWithText(Screen.Settings.name).performClick()
    }

    @Test
    fun appNavHost_clickSettings_navigatesToInfoScreen() {
        composeTestRule.onNodeWithText(Screen.Learn.name).performClick()
        navController.assertCurrentRouteName(Screen.Learn.name)
    }

    private fun navigateToInfoScreen() {
        composeTestRule.onNodeWithText(Screen.Learn.name).performClick()
    }

    @Test
    fun appNavHost_clickCalendarFABOnSettingsScreen_navigatesToCalendarScreen() {
        navigateToSettingsScreen()
        composeTestRule.onNodeWithContentDescription("Navigate to Calendar page").performClick()
        navController.assertCurrentRouteName(Screen.Calendar.name)
    }

    @Test
    fun appNavHost_clickCalendarFABOnInfoScreen_navigatesToCalendarScreen() {
        navigateToInfoScreen()
        composeTestRule.onNodeWithContentDescription("Navigate to Calendar page").performClick()
        navController.assertCurrentRouteName(Screen.Calendar.name)
    }

    @Test
    fun appNavHost_clickCalendarFABOnCalendarScreen_navigatesLoggingPage() {
        composeTestRule.onNodeWithContentDescription("Open logging options").performClick()
        composeTestRule.onNodeWithContentDescription("Log daily symptoms")
            .performClick()
        composeTestRule.onNodeWithText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)))
            .assertIsDisplayed()
    }

    @Test
    fun appNavHost_clickCalendarFABOnCalendarScreen_navigatesLogMultipleDatesPage() {
        composeTestRule.onNodeWithContentDescription("Open logging options").performClick()
        composeTestRule.onNodeWithContentDescription("Log multiple period dates")
            .performClick()
        navController.assertCurrentRouteName(Screen.LogMultipleDates.name)
    }
    fun signIn() {
    }
}
