package com.tpp.theperiodpurse.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.tpp.theperiodpurse.ui.onboarding.GoogleSignInButton
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.utility.validateUserAuthenticationAndAuthorization

/**
 * Displays the backup account screen.
 *
 * @param navController The navigation controller.
 * @param signIn The callback function for signing in.
 * @param context The Android context.
 */
@Composable
fun BackUpAccountScreen(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel,
    signIn: (launcher: ActivityResultLauncher<Intent>) -> Unit,
    signOut: () -> Unit = {},
    context: Context,
) {
    val configuration = LocalConfiguration.current
    val screenheight = configuration.screenHeightDp

    val account = GoogleSignIn.getLastSignedInAccount(context)
    val authorized = validateUserAuthenticationAndAuthorization(account)
    Log.d("Backup account", "Re-rendering")

    if (account == null) {
        SignInView(screenheight, signIn, appViewModel, context, signOut, navController)
    } else if (authorized) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = (screenheight * 0.09).dp,
                    start = (screenheight * 0.03).dp,
                    end = (screenheight * 0.03).dp,
                ),
            contentAlignment = Alignment.TopStart,
        ) {
            Column {
                Text(
                    text = "Back Up Account",
                    color = Color.DarkGray,
                    fontSize = 18.scaledSp(),
                )

                Spacer(modifier = Modifier.height((screenheight * (0.02)).dp))
                Text(
                    text = "Backing up to Google Drive will upload your data to Google Drive and ensure you can access it on other devices.",
                    fontSize = 13.scaledSp(),
                    color = appViewModel.colorPalette.MainFontColor
                )
                Spacer(modifier = Modifier.height((screenheight * (0.02)).dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = { startBackupProcess(navController) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(97, 153, 154)),
                ) {
                    Text(text = "Back Up Account", color = Color.White)
                }
                Spacer(modifier = Modifier.height((screenheight * (0.02)).dp))
                Text(
                    text = "Last backup: Not available",
                    color = Color.Gray,
                    fontSize = 13.scaledSp(),
                )
            }
        }
    } else {
        Toast.makeText(
            context,
            "ERROR - Please grant all the required permissions",
            Toast.LENGTH_SHORT,
        ).show()
        signOut()
        LaunchedEffect(Unit) {
            navController.navigate(SettingScreenNavigation.Start.name)
        }
    }
}

@Composable
private fun SignInView(screenheight: Int, signIn: (launcher: ActivityResultLauncher<Intent>) ->
Unit, appViewModel: AppViewModel, context: Context, signOut: () -> Unit, navController: NavHostController) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts
        .StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task, context, signOut, navController)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = (screenheight * 0.09).dp,
                start = (screenheight * 0.03).dp,
                end = (screenheight * 0.03).dp,
            ),
        contentAlignment = Alignment.TopStart,
    ) {
        Column {
            Text(
                text = "Back Up Account",
                color = Color.Gray,
                fontSize = 18.scaledSp(),
            )

            Spacer(modifier = Modifier.height((screenheight * (0.02)).dp))

            Text(
                text = "Sign in to backup your data",
                fontSize = 15.scaledSp(),
                color = appViewModel.colorPalette.MainFontColor
            )

            Spacer(modifier = Modifier.height((screenheight * (0.02)).dp))

            GoogleSignInButton {
                signIn(launcher)
            }
        }
    }
}
private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>, context: Context,
                               signout: () -> Unit, navController: NavHostController) {
    try {
        if (completedTask.isSuccessful) {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            val hasGoogleDrivePermission = validateUserAuthenticationAndAuthorization(account)
            if (!hasGoogleDrivePermission) {
                handleSecurityError(context, signout, "ERROR - Please grant all the required " +
                        "permissions", navController)
            } else {
                Log.d("Welcome", "Navigating First Time")
                Toast.makeText(context, "SignIn Successful", Toast.LENGTH_SHORT).show()
                navController.navigate(SettingScreenNavigation.BackUpAccount.name)
            }
        } else {
            Toast.makeText(context, "SignIn Failed", Toast.LENGTH_SHORT).show()
        }
    } catch (e: ApiException) {
        Log.d("Sign In", e.toString())
        Toast.makeText(context, "SignIn Failed", Toast.LENGTH_SHORT).show()
    }
}

fun startBackupProcess(navController: NavHostController) {
    navController.navigate(SettingScreenNavigation.BackupDatabase.name)
}
