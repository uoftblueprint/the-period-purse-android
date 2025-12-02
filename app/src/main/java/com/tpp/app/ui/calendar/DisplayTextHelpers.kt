package com.theperiodpurse.app.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

// Function to display Month with Year
@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

// Function to display Month
@RequiresApi(Build.VERSION_CODES.O)
fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}
