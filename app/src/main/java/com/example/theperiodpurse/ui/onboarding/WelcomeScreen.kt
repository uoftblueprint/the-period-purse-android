package com.example.theperiodpurse.ui.onboarding


import android.os.Build
import android.provider.Settings.Global.getString
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.theperiodpurse.GoogleSignInButton
import com.example.theperiodpurse.MainActivity
import com.example.theperiodpurse.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WelcomeScreen(onNextButtonClicked: () -> Unit, modifier: Modifier = Modifier, mainActivity: MainActivity) {

    val configuration = LocalConfiguration.current

    val screenwidth = configuration.screenWidthDp;

    val screenheight = configuration.screenHeightDp;

    lateinit var mAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    googleSignInClient = GoogleSignIn.getClient(mainActivity, gso)


    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )

    Column(
        modifier = Modifier
            .padding((screenheight*0.02).dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        Spacer(modifier = Modifier.height((screenheight*0.09).dp))

        // Logo Image
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size((screenheight*0.25).dp)
        )
        Spacer(modifier = Modifier.height((screenheight*0.09).dp))

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

        }
        Spacer(modifier = Modifier.height((screenheight*0.006).dp))

        val annotatedLinkString = buildAnnotatedString {
            val str = "Terms and Conditions and Privacy Policy"
            var startIndex = str.indexOf("Terms and Conditions")
            var endIndex = startIndex + 20
            addStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                ), start = startIndex, end = endIndex
            )
            startIndex = str.indexOf("Privacy Policy")
            endIndex = startIndex + 14
            addStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                ), start = startIndex, end = endIndex
            )
            append(str)
        }

        Text("By continuing, you accept the", textAlign = TextAlign.Center, )
        Text(text = annotatedLinkString, textAlign = TextAlign.Center)
    }
}


@Composable
fun QuickStartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 350.dp).height(50.dp),
        shape= RoundedCornerShape(15),
//        color = Color(52, 235, 161)
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(97, 153, 154))
    ) {
        Text("Quick Start", color = Color.White, fontSize = 20.sp,)
    }
}
