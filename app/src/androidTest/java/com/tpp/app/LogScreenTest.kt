package com.tpp.app

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties
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
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltAndroidTest
class LogScreenTest {
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
                skipOnboarding = true,
                skipDatabase = true,
                signIn = { signIn() },
                calendarViewModel = calendarViewModel,
                context = LocalContext.current,
                onboardViewModel = onboardViewModel,
                appViewModel = appViewModel,
            )
        }
    }

    private fun navigateToLogScreen() {
        composeTestRule.onNodeWithContentDescription(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            ).toString(),
        ).performClick()
    }

    @Test
    fun appLogScreen_clickOnArrow_changesDate() {
        navigateToLogScreen()
        val initialDate = composeTestRule.onNodeWithTag("DateLabel")
            .fetchSemanticsNode().config[SemanticsProperties.Text][0].text
        composeTestRule.onNodeWithContentDescription("Log Back Arrow").performClick()
        val finalDate = composeTestRule.onNodeWithTag("DateLabel")
            .fetchSemanticsNode().config[SemanticsProperties.Text][0].text
        assertTrue(initialDate != finalDate)
    }

    @Test
    fun appLogScreen_clickOnFlowTab_opensFlowTab() {
        navigateToLogScreen()
        composeTestRule.onNodeWithStringId(LogPrompt.Flow.title).performClick()
        composeTestRule.onNodeWithText(LogSquare.FlowHeavy.description).assertIsDisplayed()
    }

    @Test
    fun appLogScreen_clickOnSave_navigatesToCalendar() {
        navigateToLogScreen()
        composeTestRule.onNodeWithStringId(LogPrompt.Mood.title).performClick()
        composeTestRule.onNodeWithContentDescription(LogSquare.MoodHappy.description).performClick()
        composeTestRule.onNodeWithContentDescription("Save Button").performClick()
        navController.assertCurrentRouteName(Screen.Calendar.name)
    }

    @Test
    fun appLogScreen_clickOnX_navigatesToCalendar() {
        navigateToLogScreen()
        composeTestRule.onNodeWithContentDescription("Close Button").performClick()
        navController.assertCurrentRouteName(Screen.Calendar.name)
    }
    fun signIn() {
    }
}
