package com.tpp.theperiodpurse.ui.onboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tpp.theperiodpurse.OnboardingScreen
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.component.Background
import com.tpp.theperiodpurse.ui.component.handleError
import com.tpp.theperiodpurse.ui.legal.TermsAndPrivacyFooter
import com.tpp.theperiodpurse.ui.state.OnboardUIState
import com.tpp.theperiodpurse.ui.theme.MainFontColor
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.utility.validateUserAuthenticationAndAuthorization

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WelcomeScreen(
    signIn: (launcher: ActivityResultLauncher<Intent>) -> Unit,
    signout: () -> Unit = {},
    onNextButtonClicked: () -> Unit,
    navController: NavHostController,
    context: Context,
    onboardUIState: OnboardUIState,
    appViewModel: AppViewModel
) {
    val configuration = LocalConfiguration.current
    val screenheight = configuration.screenHeightDp
    val screenwidth = configuration.screenWidthDp
    val account = GoogleSignIn.getLastSignedInAccount(context)
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts
        .StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task, context, onboardUIState, signout, navController)
        }
    }
    Log.d("Welcome", "Re-rendering")

    if (account != null) {
        onboardUIState.googleAccount = account.account
        val hasGoogleDrivePermission = validateUserAuthenticationAndAuthorization(account)
        if (!hasGoogleDrivePermission) {
            handleSecurityError(
                context,
                signout,
                "ERROR - Please grant all the required " +
                    "permissions",
                navController,
            )
        } else {
            LaunchedEffect(Unit) {
                Log.d("Welcome", "Navigating Second Time")
                navController.navigate(OnboardingScreen.RestoreFromGoogleDrivePrompt.name)
            }
        }
    } else {
        Background()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(modifier = Modifier.height((screenheight * 0.1).dp))
            // Logo Image
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier.size((screenheight * 0.25).dp),
            )
            Spacer(modifier = Modifier.height((screenheight * 0.02).dp))
            // Welcome text
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                fontSize = 35.scaledSp(),
            )
            Spacer(modifier = Modifier.height((screenheight * 0.05).dp))
            // Quick Start button
            QuickStartButton(
                onClick = onNextButtonClicked,
            )
            Spacer(modifier = Modifier.height(5.dp))
            // Sign in with Google Button
            GoogleSignInButton { signIn(launcher) }

            Spacer(modifier = Modifier.height((screenheight * 0.006).dp))

            Text("By continuing, you accept the", textAlign = TextAlign.Center, fontSize = (screenwidth * 0.04).scaledSp())
            TermsAndPrivacyFooter(navController, MainFontColor)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GoogleSignInButton(
    signInClicked: () -> Unit,
) {
    Button(
        onClick = signInClicked,
        modifier = Modifier
            .widthIn(min = 350.dp)
            .height(50.dp),
        shape = RoundedCornerShape(15),
        border = BorderStroke(width = 1.dp, color = Color.Black),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "Google Button",
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Sign in with Google",
            color = Color.Black,
            fontSize = 20.scaledSp(),
        )
    }
}

@Composable
fun QuickStartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .widthIn(min = 350.dp)
            .height(50.dp)
            .semantics { contentDescription = "Next" },
        shape = RoundedCornerShape(15),
//        color = Color(52, 235, 161)
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(97, 153, 154)),
    ) {
        Text("Quick Start", color = MainFontColor, fontSize = 20.scaledSp())
    }
}

@Composable
fun Double.scaledSp(): TextUnit {
    val value: Double = this
    return with(LocalDensity.current) {
        val fontScale = this.fontScale
        val textSize = value / fontScale
        textSize.sp
    }
}

val Double.scaledSp: TextUnit
    @Composable get() = scaledSp()

@Composable
fun Int.scaledSp(): TextUnit {
    val value: Int = this
    return with(LocalDensity.current) {
        val fontScale = this.fontScale
        val textSize = value / fontScale
        textSize.sp
    }
}

val Int.scaledSp: TextUnit
    @Composable get() = scaledSp()

fun handleSecurityError(
    context: Context,
    signout: () -> Unit,
    msg: String,
    navController:
    NavHostController,
) {
    handleError(context, msg) {
        signout()
        navController.navigate(OnboardingScreen.Welcome.name)
    }
}

private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>, context: Context,
                               onboardUIState: OnboardUIState, signout: () -> Unit, navController: NavHostController) {
    try {
        if (completedTask.isSuccessful) {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            if (account != null) { onboardUIState.googleAccount = account.account }
            val hasGoogleDrivePermission = validateUserAuthenticationAndAuthorization(account)
            if (!hasGoogleDrivePermission) {
                handleSecurityError(
                    context,
                    signout,
                    "ERROR - Please grant all the required " +
                            "permissions",
                    navController,
                )
            } else {
                Log.d("Welcome", "Navigating First Time")
                Toast.makeText(context, "SignIn Successful", Toast.LENGTH_SHORT).show()
                navController.navigate(OnboardingScreen.RestoreFromGoogleDrivePrompt.name)
            }
        } else {
            Toast.makeText(context, "SignIn Failed", Toast.LENGTH_SHORT).show()
        }
    } catch (e: ApiException) {
        Log.d("Sign In", e.toString())
        Toast.makeText(context, "SignIn Failed", Toast.LENGTH_SHORT).show()
    }
}