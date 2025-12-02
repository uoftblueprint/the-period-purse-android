package com.tpp.app.ui.onboarding

import android.accounts.Account
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tpp.app.OnboardingScreen
import com.tpp.app.ui.component.LoadingScreen
import com.tpp.app.ui.viewmodel.AppViewModel
import com.tpp.app.ui.viewmodel.OnboardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RestoreFromGoogleDrivePrompt(
    googleAccount: Account?,
    onboardViewModel: OnboardViewModel,
    appViewModel: AppViewModel,
    navHostController: NavHostController,
    signout: () -> Unit = {},
    context: Context,
) {
    Log.d("RestoreFromGoogleDrive", "Rendering")
    val googleDriveFolder by onboardViewModel.googleDriveFolder.observeAsState()
    val drivePermissionHasError by onboardViewModel.drivePermissionHasError.observeAsState()
    if (googleAccount != null) {
        LaunchedEffect(key1 = googleAccount) {
            onboardViewModel.checkGoogleDrive(account = googleAccount, context = context)
        }
    } else {
        // if user is not authenticated, we shouldn't be on this page
        navHostController.navigate(OnboardingScreen.Welcome.name)
    }
    // show rendering screen while we wait
    if (googleDriveFolder == null) {
        LoadingScreen(appViewModel = appViewModel)
    } else {
        // no permission, give error and navigate away
        if (drivePermissionHasError == true) {
            Toast.makeText(context, "ERROR - Please grant all the required permissions", Toast.LENGTH_SHORT).show()
            signout()
            navHostController.navigate(OnboardingScreen.Welcome.name)
        }
        // no file, don't ask to restore back up
        // now we know google drive is loaded
        else if (!googleDriveFolder!!.files.isNotEmpty()) {
            navHostController.navigate(OnboardingScreen.QuestionOne.name)
        } else {
            BackUpFromGoogleDrivePrompt(navHostController)
        }
    }
}

@Composable
private fun BackUpFromGoogleDrivePrompt(navHostController: NavHostController) {
    AlertDialog(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White,
        contentColor = Color.Black,
        onDismissRequest = { handleDismiss(navHostController) },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Use Backup",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                )
            }
        },
        text = {
            Text(
                text = "Would you like to use your backed up data for this app?",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = { ConfirmButton(navHostController) },
        dismissButton = { DismissButton(navHostController) },
    )
}

@Composable
private fun ConfirmButton(navHostController: NavHostController) {
    OutlinedButton(
        onClick = { handleBackUpFromDatabase(navHostController) },
        modifier = Modifier
            .padding(2.dp)
            .height(48.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
    ) {
        Text(
            text = "Yes",
            style = MaterialTheme.typography.button,
            color = Color.Blue,
        )
    }
}

@Composable
private fun DismissButton(navHostController: NavHostController) {
    OutlinedButton(
        onClick = { handleDismiss(navHostController) },
        modifier = Modifier
            .padding(2.dp)
            .height(48.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
    ) {
        Text(
            text = "No",
            style = MaterialTheme.typography.button,
            color = Color(195, 50, 50),
        )
    }
}

fun handleBackUpFromDatabase(navHostController: NavHostController) {
    Log.d("RestoreFromGoogleDrive", "Navigating to download back up")
    navHostController.navigate(OnboardingScreen.DownloadBackupFromGoogleDrive.name)
}

fun handleDismiss(navHostController: NavHostController) {
    navHostController.navigate(OnboardingScreen.QuestionOne.name)
}
