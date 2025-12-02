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
import com.tpp.app.ui.calendar.CalendarTabItem
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltAndroidTest
class CalendarScreenTest {
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
            navController =
                TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator(),
            )
            AppScreen(
                onboardViewModel = onboardViewModel,
                appViewModel = appViewModel,
                navController = navController,
                calendarViewModel = calendarViewModel,
                signIn = { signIn() },
                skipDatabase = true,
                skipOnboarding = true,
                context = LocalContext.current,
            )
        }
    }

    @Test
    fun appCalendarScreen_clickOnArrow_showAndHideSymptomOptions() {
        composeTestRule.onNodeWithContentDescription("Expand to switch symptoms")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Mood")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Cramps")
            .assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("Selected Symptom")
        hasContentDescription("Cramps")
    }

    @Test
    fun calendarIsDisplayed() {
        composeTestRule.onNodeWithContentDescription("Calendar Page").assertIsDisplayed()
    }

    // Check last entry month (Current month)
    @Test
    fun displayCurrentMonth() {
        composeTestRule.onNodeWithContentDescription(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            ),
        ).assertIsDisplayed()
    }

    // Check scrolling works
    @Test
    fun scrollTo_ActionAssertion() {
        composeTestRule.onNodeWithContentDescription("Calendar")
            .assert(hasScrollAction())
    }

    // Check if you can go to Cycle Tab
    @Test
    fun appTabs_DisplayCycleTab() {
        composeTestRule.onNodeWithText(CalendarTabItem.CycleTab.title).performClick()
        composeTestRule.onNodeWithContentDescription("Cycle Page").assertIsDisplayed()
    }

    // Click on date to bring up the tracking menu
    @Test
    fun appScreen_DisplayLogScreen() {
        composeTestRule.onNodeWithContentDescription(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            ).toString(),
        ).performClick()
    }

    @Test
    fun calendarScreen_OpenLoggingOptionsOverlay() {
        composeTestRule.onNodeWithContentDescription("Open logging options").performClick()
        composeTestRule.onNodeWithContentDescription("Log daily symptoms").performClick()
        var date = composeTestRule
            .onNodeWithTag("DateLabel").fetchSemanticsNode()
            .config[SemanticsProperties.Text][0].text
        var today = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        assert(date == today)
    }

    fun signIn() {
    }
}
