package com.theperiodpurse.app.ui.cycle.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.theperiodpurse.app.R
import com.theperiodpurse.app.utility.calculateArcAngle
import com.theperiodpurse.app.utility.calculateDaysSinceLastPeriod
import com.theperiodpurse.app.data.entity.Date
import com.theperiodpurse.app.ui.onboarding.scaledSp
import com.theperiodpurse.app.ui.viewmodel.AppViewModel

@Composable
fun CurrentCycleBox(modifier: Modifier = Modifier, dates: ArrayList<Date>, appViewModel: AppViewModel) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(5),
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        backgroundColor = if (appViewModel.getColorMode()) appViewModel.colorPalette
            .HeaderColor1.copy(0.75f) else Color.White,
    ) {

        Column {
            Text(
                text = stringResource(R.string.current_cycle),
                fontSize = 20.scaledSp(),
                fontWeight = FontWeight(700),
                color = Color(0xFFB12126),
                modifier = modifier.padding(start = 20.dp, top = 20.dp),
            )
            CycleInfo(dates = dates, modifier = modifier, appViewModel = appViewModel)
        }

    }
}

@Composable
private fun CycleInfo(
    dates: ArrayList<Date>,
    modifier: Modifier,
    appViewModel: AppViewModel
) {
    val gradientColor = listOf(appViewModel.colorPalette.primary2, appViewModel.colorPalette.primary1)
    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier.Companion
                .align(Alignment.Center)
                .size(200.dp),
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val strokeWidth = 23.dp.toPx()
                val ringColor = Color(0xFFB12126)
                val radius = (size.minDimension - strokeWidth) / 2

                drawArc(
                    color = ringColor.copy(alpha = 0.2f),
                    startAngle = -80f,
                    sweepAngle = 340f,
                    useCenter = false,
                    topLeft = center - Offset(radius, radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                // Draw the progress ring
                drawArc(
                    brush = Brush.linearGradient(gradientColor),
                    startAngle = -80f,
                    sweepAngle = calculateArcAngle(dates),
                    useCenter = false,
                    topLeft = center - Offset(radius, radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }
            Column(
                modifier = modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = calculateDaysSinceLastPeriod(dates).toString(),
                    fontSize = 40.scaledSp(),
                    modifier = Modifier.padding(0.dp),
                    fontWeight = FontWeight(900),
                    color = Color(0xFFB12126),
                )
                Text(
                    text = stringResource(R.string.days_since),
                    fontSize = 15.scaledSp(),
                    fontWeight = FontWeight(500),
                    color = appViewModel.colorPalette.MainFontColor
                )
                Text(
                    text = stringResource(R.string.last_period),
                    fontSize = 15.scaledSp(),
                    fontWeight = FontWeight(500),
                    color = appViewModel.colorPalette.MainFontColor
                )
            }
        }
    }
}
