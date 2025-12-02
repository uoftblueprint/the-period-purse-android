package com.theperiodpurse.app.ui.setting

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.theperiodpurse.app.ui.component.LoadingScreen
import com.theperiodpurse.app.ui.component.handleError
import com.theperiodpurse.app.ui.viewmodel.AppViewModel
import com.theperiodpurse.app.ui.viewmodel.OnboardViewModel
import com.theperiodpurse.app.utility.validateUserAuthenticationAndAuthorization

// TODO Figure out why sometimes database is not backed up
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BackupDatabase(
    onboardViewModel: OnboardViewModel,
    appViewModel: AppViewModel,
    navController: NavHostController,
    signout: () -> Unit = {},
    context: Context,
) {
    val hasBackedUpToGoogleDrive by onboardViewModel.hasBackedUpToGoogleDrive.observeAsState()
    val googleSignedInAccount = GoogleSignIn.getLastSignedInAccount(context)
    val account = googleSignedInAccount?.account
    val hasGoogleDrivePermission = validateUserAuthenticationAndAuthorization(googleSignedInAccount)
    if (account == null) {
        handleSecurityError(context, signout, "ERROR - Please sign in", navController)
    }
    if (!hasGoogleDrivePermission) {
        handleSecurityError(context, signout, "ERROR - Please grant all the required permissions", navController)
    }

    LaunchedEffect(key1 = hasBackedUpToGoogleDrive) {
        if (account != null && !hasBackedUpToGoogleDrive!!) {
            onboardViewModel.backupDatabase(account = account, context)
        } else {
            onboardViewModel.hasBackedUpToGoogleDrive.postValue(false)
            navController.navigate(SettingScreenNavigation.ConfirmBackup.name)
        }
    }
    LoadingScreen(appViewModel = appViewModel)
}

fun handleSecurityError(
    context: Context,
    signout: () -> Unit,
    msg: String,
    navController:
    NavHostController,
) {
    handleError(context, msg) {
        signout()
        navController.navigate(SettingScreenNavigation.Start.name)
    }
}
