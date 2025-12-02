package com.tpp.app

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.tpp.app.ui.setting.SettingScreenNavigation
import com.tpp.app.ui.setting.SettingsScreen
import com.tpp.app.ui.viewmodel.AppViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SettingsNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Inject
    lateinit var appViewModel: AppViewModel

    // Used to manage the  components' state and is used to perform injection on tests
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
            SettingsScreen(
                outController = navController,
                navController = navController,
                context = LocalContext.current,
                appViewModel = appViewModel,
                onboardUiState = null,
                onboardViewModel = null,
                appUiState = null,
                calUiState = null,
                signIn = {},
            )
        }
    }

    @Test
    fun appNavhost_clickNotifications_navigatesToNotificationsScreen() {
        navController.assertCurrentRouteName(SettingScreenNavigation.Start.name)
        composeTestRule.onNodeWithStringId(R.string.customize_notifications).performClick()
        navController.assertCurrentRouteName(SettingScreenNavigation.Notification.name)
    }

    @Test
    fun appNavhost_clickBackUp_navigatesToBackUpScreen() {
        navController.assertCurrentRouteName(SettingScreenNavigation.Start.name)
        composeTestRule.onNodeWithStringId(R.string.back_up_account).performClick()
        navController.assertCurrentRouteName(SettingScreenNavigation.BackUpAccount.name)
    }

    @Test
    fun appNavhost_clickDelete_navigatesToDeleteScreen() {
        navController.assertCurrentRouteName(SettingScreenNavigation.Start.name)
        composeTestRule.onNodeWithStringId(R.string.delete_account).performClick()
        navController.assertCurrentRouteName(SettingScreenNavigation.DeleteAccount.name)
    }
}
