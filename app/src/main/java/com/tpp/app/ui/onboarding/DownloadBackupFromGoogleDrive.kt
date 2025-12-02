package com.theperiodpurse.app.ui.onboarding

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.theperiodpurse.app.OnboardingScreen
import com.theperiodpurse.app.ui.component.LoadingScreen
import com.theperiodpurse.app.ui.viewmodel.AppViewModel
import com.theperiodpurse.app.ui.viewmodel.OnboardViewModel
import com.theperiodpurse.app.utility.validateUserAuthenticationAndAuthorization

@Composable
fun DownloadBackupFromGoogleDrive(
    onboardViewModel: OnboardViewModel,
    appViewModel: AppViewModel,
    navHostController: NavHostController,
    signout: () -> Unit = {},
) {
    Log.d("DownloadBackupFromGD", "Rendering download backup")
    val context = LocalContext.current.applicationContext
    val googleDriveLoadSuccess by onboardViewModel.googleDriveLoadSuccess.observeAsState(initial = null)
    val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
    val googleAccount = googleSignInAccount?.account
    val hasGoogleDrivePermission = validateUserAuthenticationAndAuthorization(googleSignInAccount)
    if (!hasGoogleDrivePermission) {
        Toast.makeText(
            context,
            "ERROR - Please grant all the required permissions",
            Toast.LENGTH_SHORT,
        ).show()
        signout()
        navHostController.navigate(OnboardingScreen.Welcome.name)
    }
    LaunchedEffect(Unit) {
        if (googleAccount != null) {
            Log.d("DownloadBackupFromGD", "Downloading backup")
            onboardViewModel.downloadBackup(googleAccount, context)
        }
    }
    Log.d("DownloadBackupFromGD", googleDriveLoadSuccess.toString())
    when (googleDriveLoadSuccess) {
        true -> {
            LaunchedEffect(Unit) {
                onboardViewModel.googleDriveLoadSuccess.postValue(null)
                navHostController.popBackStack(OnboardingScreen.Welcome.name, inclusive = true)
                Log.d("DownloadBackupFromGD", "Navigating to load database from storage")
                navHostController.navigate(OnboardingScreen.LoadDatabase.name)
            }
        }
        false -> {
            onboardViewModel.googleDriveLoadSuccess.postValue(null)
            Toast.makeText(context, "ERROR - Something went wrong", Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
        }
        else -> LoadingScreen(appViewModel = appViewModel)
    }
}
