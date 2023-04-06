package com.tpp.theperiodpurse.ui.legal

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.LegalScreen
import com.tpp.theperiodpurse.ui.education.teal

@Composable
fun TermsAndPrivacyFooter(navController: NavHostController) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClickableText(
            onClick = { navController.navigate(LegalScreen.Terms.name) },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = Color(teal),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            ),
            text = AnnotatedString("Terms and Conditions")
        )

        Text(
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            text = " and "
        )

        ClickableText(
            onClick = { navController.navigate(LegalScreen.Privacy.name) },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                color = Color(teal),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            ),
            text = AnnotatedString("Privacy Policy"),
        )
    }

}