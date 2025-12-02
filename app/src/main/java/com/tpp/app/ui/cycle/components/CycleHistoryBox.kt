package com.theperiodpurse.app.ui.cycle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.theperiodpurse.app.R
import com.theperiodpurse.app.data.entity.Date
import com.theperiodpurse.app.utility.parseDatesIntoPeriods
import com.theperiodpurse.app.ui.education.teal
import com.theperiodpurse.app.ui.onboarding.scaledSp
import com.theperiodpurse.app.ui.viewmodel.AppViewModel

@Composable
fun CycleHistoryBox(
    modifier: Modifier = Modifier,
    dates: ArrayList<Date>?,
    onClickShowFull: (Int) -> Unit,
    appViewModel: AppViewModel
) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(10),
    ) {
        Column(modifier.background(color = appViewModel.colorPalette.HeaderColor1).padding(horizontal = 15.dp, vertical = 15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.period_history),
                    fontSize = 15.scaledSp(),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF868083),
                )
                ClickableText(
                    onClick = onClickShowFull,
                    style = TextStyle(
                        color = Color(teal),
                        fontWeight = FontWeight(700),
                        fontSize = 15.scaledSp(),
                    ),
                    text = AnnotatedString(stringResource(id = R.string.show_more)),
                )
            }
            Divider(
                color = Color(0xFF868083),
                modifier = modifier
                    .padding(top = 5.dp, bottom = 10.dp)
                    .fillMaxWidth(),
            )
            // Show three most recent periods
            if (dates != null) {
                val periods = parseDatesIntoPeriods(dates)
                PeriodEntries(periods, 3, appViewModel)
            }
        }
    }
}
