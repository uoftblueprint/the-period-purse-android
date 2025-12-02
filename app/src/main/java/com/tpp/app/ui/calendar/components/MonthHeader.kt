package com.tpp.app.ui.calendar.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarMonth
import com.tpp.app.ui.calendar.displayText
import com.tpp.app.ui.onboarding.scaledSp
import com.tpp.app.ui.viewmodel.AppViewModel
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthHeader(calendarMonth: CalendarMonth, appViewModel: AppViewModel) {
    // The header that is displayed for every month; contains week & month.
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    Column(
        modifier = Modifier
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MonthAndDayHeadings(calendarMonth, daysOfWeek, appViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthAndDayHeadings(calendarMonth: CalendarMonth, daysOfWeek: List<DayOfWeek>, appViewModel: AppViewModel) {
    // Month
    Text(
        modifier = Modifier.padding(12.dp),
        textAlign = TextAlign.Center,
        fontSize = 18.scaledSp(),
        fontWeight = FontWeight.W500,
        text = calendarMonth.yearMonth.displayText(),
        color = appViewModel.colorPalette.CalendarFontColor
    )

    // Days of Week
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .offset(y = 10.dp),
                textAlign = TextAlign.Center,
                fontSize = 10.scaledSp(),
                fontWeight = FontWeight.ExtraBold,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = appViewModel.colorPalette.CalendarFontColor
            )
        }
    }
}
