package com.theperiodpurse.app.ui.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.theperiodpurse.app.LegalScreen
import com.theperiodpurse.app.ui.onboarding.scaledSp

@Composable
fun TermsAndPrivacyFooter(navController: NavHostController, textColor: Color) {
    val configuration = LocalConfiguration.current

    val screenwidth = configuration.screenWidthDp

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ClickableText(
            onClick = { navController.navigate(LegalScreen.Terms.name) },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = (screenwidth * 0.04).scaledSp(),
                fontWeight = FontWeight.Bold,
            ),
            text = AnnotatedString("Terms and Conditions"),
        )

        Text(
            textAlign = TextAlign.Center,
            fontSize = (screenwidth * 0.04).scaledSp(),
            text = " and ",
            color = textColor
        )

        ClickableText(
            onClick = { navController.navigate(LegalScreen.Privacy.name) },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = (screenwidth * 0.04).scaledSp(),
                fontWeight = FontWeight.Bold,
            ),
            text = AnnotatedString("Privacy Policy"),
        )
    }
}
