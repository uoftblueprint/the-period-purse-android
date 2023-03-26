package com.tpp.theperiodpurse

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.ui.calendar.CalendarViewModel
import com.tpp.theperiodpurse.ui.onboarding.OnboardViewModel
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
        Symptom.EXERCISE, Symptom.SLEEP, Symptom.MOOD,
        Symptom.CRAMPS
    );
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
            notes = ""
        )
    )

    private fun insertDate() {
        runBlocking {
            dateRepository.addDate(dateList[0])
        }
    }

    private fun insertUser() {
        val user = User(
            symptomsToTrack = symptomList,
            periodHistory = dateList,
            averagePeriodLength = 0,
            averageCycleLength = 0,
            daysSinceLastPeriod = 0
        )
        runBlocking {
            userRepository.addUser(user)
        }
    }

    @get:Rule
    // Used to manage the components' state and is used to perform injection on tests
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setupNavHost() {
        hiltRule.inject()
        insertDate()
        insertUser()
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )
            ScreenApp(
                navController = navController,
                skipOnboarding = true,
                onboardViewModel = onboardViewModel,
                appViewModel = appViewModel,
                calendarViewModel = calendarViewModel
            ) { signIn() }
        }
    }

    private fun navigateToLogScreen() {
        composeTestRule.onNodeWithContentDescription(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ).toString()
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
        composeTestRule.onNodeWithContentDescription("Log Close Button").performClick()
        navController.assertCurrentRouteName(Screen.Calendar.name)
    }
    fun signIn(){

    }
}