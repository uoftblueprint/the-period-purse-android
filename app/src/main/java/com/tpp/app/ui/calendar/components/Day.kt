package com.tpp.app.ui.calendar.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpp.app.R
import com.tpp.app.ui.onboarding.scaledSp
import java.time.LocalDate

val greyedOutColor = Color(237, 237, 237)

fun dayDisabled(date: LocalDate): Boolean {
    return date.isAfter(LocalDate.now())
}

@Composable
fun Day(
    date: LocalDate,
    color: Color,
    @DrawableRes iconId: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPredicted: Boolean = false
) {
    val (dayColor, textColor) = if (isPredicted || !dayDisabled(date)) {
        color.copy(alpha = 0.8f) to Color.Black
    } else {
        Color.Transparent to Color(190, 190, 190)
    }

    Box(
        modifier = Modifier
            .padding(1.dp)
            .aspectRatio(1f),
    ) {
        Box(
            modifier = modifier
                .size(64.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .fillMaxSize()
                .background(dayColor)
                .semantics { contentDescription = date.toString() }
                .border(
                    color = Color(200, 205, 205),
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                )
                .clickable(
                    enabled = !dayDisabled(date),
                    onClick = onClick,
                ),
        ) {
            val boxModifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center)
            DayInteriorStyling(date = date, boxModifier = boxModifier, iconId = iconId, textColor)
        }
    }
}

@Composable
fun DayInteriorStyling(date: LocalDate, boxModifier: Modifier, iconId: Int?, textColor: Color) {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        fontSize = 12.scaledSp(),
        fontWeight = FontWeight.Bold,
        text = date.dayOfMonth.toString(),
        color = textColor
    )
    DayImage(boxModifier = boxModifier, iconId = iconId, date = date)
}

@Composable
fun DayImage(boxModifier: Modifier, iconId: Int?, date: LocalDate) {
    Box(
        modifier = boxModifier,
    ) {
        if (iconId != null && !dayDisabled(date)) {
            Image(
                painterResource(id = iconId),
                modifier = Modifier
                    .size(20.dp)
                    .offset(y = 2.dp),
                contentDescription = stringResource(R.string.day_flow_icon_content_decription),
            )
        }
    }
}
