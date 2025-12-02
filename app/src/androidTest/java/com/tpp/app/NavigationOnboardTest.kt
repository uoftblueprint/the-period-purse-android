package com.tpp.app

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.tpp.app.data.entity.Date
import com.tpp.app.data.entity.User
import com.tpp.app.data.model.*
import com.tpp.app.data.repository.DateRepository
import com.tpp.app.data.repository.UserRepository
import com.tpp.app.ui.viewmodel.AppViewModel
import com.tpp.app.ui.viewmodel.CalendarViewModel
import com.tpp.app.ui.viewmodel.OnboardViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import javax.inject.Inject

@HiltAndroidTest
class NavigationOnboardTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Inject
    lateinit var appViewModel: AppViewModel

    @Inject
    lateinit var onboardViewModel: OnboardViewModel

    @Inject
    lateinit var calendarViewModel: CalendarViewModel

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var dateRepository: DateRepository

    private val symptomList = arrayListOf(
        Symptom.EXERCISE,
        Symptom.SLEEP,
        Symptom.MOOD,
        Symptom.CRAMPS,
    )
    private val currentDate = java.util.Date()
    private val duration: Duration = Duration.ofHours(1)
    private val dateList = arrayListOf(
        Date(
            date = currentDate,
            flow = FlowSeverity.Heavy,
            mood = Mood.ANGRY,
            exerciseLength = duration,
            exerciseType = Exercise.YOGA,
            crampSeverity = CrampSeverity.Bad,
            sleep = duration,
            notes = "",
        ),
    )

    private fun insertDate(context: Context) {
        runBlocking {
            dateRepository.addDate(dateList[0], context)
        }
    }

    private fun insertUser(context: Context) {
        val user = User(
            symptomsToTrack = symptomList,
            periodHistory = dateList,
            averagePeriodLength = 0,
            averageCycleLength = 0,
            daysSinceLastPeriod = 0,
        )
        runBlocking {
            userRepository.addUser(user, context)
        }
    }

    // Used to manage the components' state and is used to perform injection on tests
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setupNavHost() {
        hiltRule.inject()

        composeTestRule.setContent {
            insertUser(LocalContext.current)
            insertDate(LocalContext.current)
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator(),
            )
            AppScreen(
                navController = navController,
                skipOnboarding = false,
                skipWelcome = true,
                onboardViewModel = onboardViewModel,
                appViewModel = appViewModel,
                signIn = { signIn() },
                skipDatabase = true,
                context = LocalContext.current,
                calendarViewModel = calendarViewModel,
            )
        }
    }

    @Test
    fun appNavHost_Onboard_navigatesToSummary_viewModel() {
        // welcome page
        navController.assertCurrentRouteName(OnboardingScreen.QuestionOne.name)

        // text field input test and next to question two page
        val textFieldValue = "5"
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performTextInput(textFieldValue)
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performImeAction()
        composeTestRule.onNodeWithContentDescription("Next").performClick()

        navController.assertCurrentRouteName(OnboardingScreen.QuestionTwo.name)

        composeTestRule.onNodeWithContentDescription("Skip").performClick()

        // calendar input and next to question 3 page

//        composeTestRule.onNodeWithContentDescription("datepick").performClick()
//
//        // Click on the "OK" button to close the dialog
//        composeTestRule.onNodeWithText("cancel").performClick()
//        composeTestRule.onNodeWithContentDescription("Next").performClick()

        navController.assertCurrentRouteName(OnboardingScreen.QuestionThree.name)

        composeTestRule.onNodeWithContentDescription("Mood").performClick()

        composeTestRule.onNodeWithContentDescription("fitness").performClick()
// //
// //
        composeTestRule.onNodeWithContentDescription("Cramps").performClick()
//
        composeTestRule.onNodeWithContentDescription("Sleep").performClick()
//

//        composeTestRule.onNodeWithContentDescription("Next").performClick()
        composeTestRule.onNodeWithContentDescription("Next").performClick()
        navController.assertCurrentRouteName(OnboardingScreen.Summary.name)
        composeTestRule.onNodeWithText("5 days").assertExists()
        composeTestRule.onNodeWithContentDescription("Mood").assertExists()
        composeTestRule.onNodeWithContentDescription("Cramps").assertExists()
        composeTestRule.onNodeWithContentDescription("Sleep").assertExists()
        composeTestRule.onNodeWithContentDescription("Exercise").assertExists()
    }

    @Test
    fun appNavHost_clickOnboard_navigatesToSummaryBack() {
        composeTestRule.runOnIdle {
            navController.assertCurrentRouteName(OnboardingScreen.QuestionOne.name)
        }

        // next to quesiton two page
        composeTestRule.onNodeWithContentDescription("Skip").performClick()

        composeTestRule.runOnIdle {
            navController.assertCurrentRouteName(OnboardingScreen.QuestionTwo.name)
        }
//

        composeTestRule.onNodeWithContentDescription("Skip").performClick()

//

        navController.assertCurrentRouteName(OnboardingScreen.QuestionThree.name)
        composeTestRule.onNodeWithContentDescription("Skip").performClick()
        navController.assertCurrentRouteName(OnboardingScreen.Summary.name)
        // back to quesiton three page
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        navController.assertCurrentRouteName(OnboardingScreen.QuestionThree.name)
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // back to quesiton two page
        navController.assertCurrentRouteName(OnboardingScreen.QuestionTwo.name)

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // back to quesiton one page
        navController.assertCurrentRouteName(OnboardingScreen.QuestionOne.name)
    }

    @Test
    fun appNavHost_blank_input_questionone() {
        navController.assertCurrentRouteName(OnboardingScreen.QuestionOne.name)
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performImeAction()
        navController.assertCurrentRouteName(OnboardingScreen.QuestionOne.name)
    }

    @Test
    fun appNavHost_questionone_input_change() {
        navController.assertCurrentRouteName(OnboardingScreen.QuestionOne.name)
        val textFieldValue = "5"
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performTextInput(textFieldValue)
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performImeAction()

        composeTestRule.onNodeWithContentDescription("Next").performClick()

        composeTestRule.onNodeWithContentDescription("Skip").performClick()

        composeTestRule.onNodeWithContentDescription("Skip").performClick()

        navController.assertCurrentRouteName(OnboardingScreen.Summary.name)

        composeTestRule.onNodeWithText("5 days").assertExists()

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // back to quesiton one page
        navController.assertCurrentRouteName(OnboardingScreen.QuestionOne.name)

        val newtextFieldValue = "6"
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performTextClearance()
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performTextInput(newtextFieldValue)
        composeTestRule.onNodeWithContentDescription("Pick Days")
            .performImeAction()

        composeTestRule.onNodeWithContentDescription("Next").performClick()

        composeTestRule.onNodeWithContentDescription("Skip").performClick()

        composeTestRule.onNodeWithContentDescription("Skip").performClick()

        navController.assertCurrentRouteName(OnboardingScreen.Summary.name)

        composeTestRule.onNodeWithText("6 days").assertExists()
    }

    fun signIn() {
    }
}
