package com.tpp.theperiodpurse.ui.symptomlog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.theme.Red
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

@Composable
fun LoggingOptionsPopup(
    onLogDailySymptomsClick: () -> Unit,
    onLogMultiplePeriodDates: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel
) {
    Popup(
        alignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .background(appViewModel.colorPalette.HeaderColor1.copy(0.8f))
                .then(modifier)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                ) { onExit() },
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .clickable(enabled = false) {}
                    .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
            ) {
                LoggingOptionButton(
                    title = "Log daily symptoms",
                    icon = R.drawable.water_drop_black_24dp,
                    onClick = {
                        onLogDailySymptomsClick()
                        onExit()
                    },
                    appViewModel = appViewModel
                )
                Spacer(modifier = Modifier.width(30.dp))
                LoggingOptionButton(
                    title = "Log multiple period dates",
                    icon = R.drawable.today_black_24dp,
                    onClick = {
                        onExit()
                        onLogMultiplePeriodDates()
                    },
                    appViewModel = appViewModel
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clickable(enabled = false) {},
            )
            CloseOverlayButton(onClick = onExit)
        }
    }
}

@Composable
fun CloseOverlayButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = Color.White,
        modifier = modifier.size(70.dp),
    ) {
        Icon(
            painterResource(R.drawable.close_black_24dp),
            contentDescription = "Close logging options",
            tint = Red,
        )
    }
}

@Composable
private fun LoggingOptionButton(
    title: String,
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp),
            fontSize = 15.scaledSp(),
            color = appViewModel.colorPalette.MainFontColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = Red,
            modifier = modifier.size(70.dp),
        ) {
            Icon(
                painterResource(icon),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
