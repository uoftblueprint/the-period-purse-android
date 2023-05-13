package com.tpp.theperiodpurse.ui.onboarding


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.Status
import com.tpp.theperiodpurse.OnboardingScreen
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.OnboardUIState
import com.tpp.theperiodpurse.ui.legal.TermsAndPrivacyFooter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WelcomeScreen(
    signIn: () -> Unit,
    onNextButtonClicked: () -> Unit,
    navController: NavHostController,
    context: Context,
    onboardUIState: OnboardUIState
) {

    val configuration = LocalConfiguration.current

    val screenheight = configuration.screenHeightDp

    val account = GoogleSignIn.getLastSignedInAccount(context)

    if (account != null){
        onboardUIState.googleAccount = account.account
        LaunchedEffect(Unit){
            navController.navigate(OnboardingScreen.LoadGoogleDrive.name)
        }
    }
    else {
        val signInResult = remember { mutableStateOf(GoogleSignInResult(GoogleSignInAccount.createDefault(), Status.RESULT_CANCELED)) }
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .padding((screenheight * 0.02).dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            Spacer(modifier = Modifier.height((screenheight*0.05).dp))

            // Logo Image
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier.size((screenheight*0.25).dp)
            )
            Spacer(modifier = Modifier.height((screenheight*0.05).dp))

            // Welcome text
            Text(text = stringResource(R.string.welcome), style = MaterialTheme.typography.h4, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height((screenheight*0.13).dp))

            // Quick Start button
            QuickStartButton(
                onClick = onNextButtonClicked
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Sign in with Google Button
            GoogleSignInButton {
                signIn()
            }

            LaunchedEffect(signInResult.value) {
                if (signInResult.value.isSuccess) {
                    navController.navigate(OnboardingScreen.QuestionTwo.name)
                }
                else {
                    signInResult.value = GoogleSignInResult(GoogleSignInAccount.createDefault(), Status.RESULT_CANCELED)
                }
            }

            Spacer(modifier = Modifier.height((screenheight*0.006).dp))


            Text("By continuing, you accept the", textAlign = TextAlign.Center )
            TermsAndPrivacyFooter(navController)
        }



    }

}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GoogleSignInButton(
    signInClicked: () -> Unit
) {

    Button(
        onClick = signInClicked,
        modifier = Modifier
            .widthIn(min = 350.dp)
            .height(50.dp),
        shape= RoundedCornerShape(15),
        border = BorderStroke(width = 1.dp, color=Color.LightGray),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "Google Button",
            tint=Color.Unspecified,

            )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Sign in with Google",
            color = Color.Black, fontSize = 20.sp,
        )
    }
}

@Composable
fun QuickStartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .widthIn(min = 350.dp)
            .height(50.dp)
            .semantics { contentDescription = "Next" },
        shape= RoundedCornerShape(15),
//        color = Color(52, 235, 161)
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(97, 153, 154))
    ) {
        Text("Quick Start", color = Color.White, fontSize = 20.sp)
    }
}

