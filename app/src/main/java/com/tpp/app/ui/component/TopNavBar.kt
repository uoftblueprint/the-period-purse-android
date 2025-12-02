package com.theperiodpurse.app.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.theperiodpurse.app.R
import com.theperiodpurse.app.ui.onboarding.scaledSp
import com.theperiodpurse.app.ui.theme.Teal
import com.theperiodpurse.app.ui.viewmodel.AppViewModel

@Composable
fun TopNavBar(
    screenName: String,
    navController: NavController,
    interactionSource:
    MutableInteractionSource,
    appViewModel: AppViewModel
) {
    TopAppBar(
        backgroundColor = appViewModel.colorPalette.HeaderColor1,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) { navController.popBackStack() }
                    .size(20.dp),
                painter = painterResource(R.drawable.arrow),
                contentDescription = stringResource(R.string.back_button),
                tint = Teal,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                fontSize = 20.scaledSp(),
                fontWeight = FontWeight.Bold,
                color = appViewModel.colorPalette.MainFontColor,
                text = screenName,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
