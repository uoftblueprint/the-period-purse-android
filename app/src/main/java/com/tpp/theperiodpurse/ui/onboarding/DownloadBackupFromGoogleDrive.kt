package com.tpp.theperiodpurse.ui.onboarding

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.tpp.theperiodpurse.OnboardingScreen
import com.tpp.theperiodpurse.ui.component.LoadingScreen
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel
import com.tpp.theperiodpurse.utility.validateUserAuthenticationAndAuthorization

@Composable
fun DownloadBackupFromGoogleDrive(
    viewModel: OnboardViewModel,
    navHostController: NavHostController,
    signout: () -> Unit = {},
    context: Context
) {
    Log.d("DownloadBackupFromGD", "Rendering download backup")
    val googleDriveLoadSuccess by viewModel.googleDriveLoadSuccess.observeAsState(initial = null)
    val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
    val googleAccount = googleSignInAccount?.account
    val hasGoogleDrivePermission = validateUserAuthenticationAndAuthorization(googleSignInAccount)
    if (!hasGoogleDrivePermission) {
        Toast.makeText(
            context,
            "ERROR - Please grant all the required permissions",
            Toast.LENGTH_SHORT
        ).show()
        signout()
        navHostController.navigate(OnboardingScreen.Welcome.name)
    }
    LaunchedEffect(Unit) {
        if (googleAccount != null) {
            Log.d("DownloadBackupFromGD" ,"Downloading backup")
            viewModel.downloadBackup(googleAccount, context)
        }
    }
    Log.d("DownloadBackupFromGD", googleDriveLoadSuccess.toString())
    when (googleDriveLoadSuccess) {
        true -> {
            LaunchedEffect(Unit) {
                viewModel.googleDriveLoadSuccess.postValue(null)
                navHostController.popBackStack(OnboardingScreen.Welcome.name, inclusive = true)
                Log.d("DownloadBackupFromGD", "Navigating to load database from storage")
                navHostController.navigate(OnboardingScreen.LoadDatabase.name)
            }
        }
        false -> {
            viewModel.googleDriveLoadSuccess.postValue(null)
            Toast.makeText(context, "ERROR - Something went wrong", Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
        }
        else -> LoadingScreen()
    }
}