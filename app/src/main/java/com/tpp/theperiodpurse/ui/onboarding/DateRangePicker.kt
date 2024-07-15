package com.tpp.theperiodpurse.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.tpp.theperiodpurse.ui.state.OnboardUIState
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    onSendButtonClicked: () -> Unit,
    viewModel: OnboardViewModel,
    uiState: OnboardUIState,
) {
    val configuration = LocalConfiguration.current
    val screenwidth = configuration.screenWidthDp
    val calendar = Calendar.getInstance()
    val snackState = remember { SnackbarHostState() }
    val dayRange = (uiState.days - 1).toLong()
    SnackbarHost(hostState = snackState, Modifier.zIndex(1f))
    val state = rememberDateRangePickerState(
        yearRange = IntRange(
            calendar.get(Calendar.YEAR) - 3,
            calendar.get(Calendar.YEAR),
        ),
        initialSelectedStartDateMillis = null,
        initialSelectedEndDateMillis = null,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis < System.currentTimeMillis()
            }
        },
    )
    if (uiState.date != "" && uiState.date != "Choose date" && state.selectedStartDateMillis == null) {
        val dates = uiState.date.split(" to ")
        val date = convertDateToEpochMillis(dates[0])
        val endDate = convertDateToEpochMillis(dates[1])
        if (dayRange > 0) {
            state.setSelection(
                date,
                addDaysToSelectedStartDate(date, dayRange),
            )
        } else {
            state.setSelection(
                date,
                endDate,
            )
        }
    }
    Box(
        modifier = Modifier.background(Color.White).safeDrawingPadding(),
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
            // Add a row with "Save" and dismiss actions.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = { onSendButtonClicked() }) {
                    Icon(Icons.Filled.Close, contentDescription = "Localized description")
                }
                TextButton(
                    onClick = {
                        uiState.dateOptions = getDatesBetween(
                            state.selectedStartDateMillis!!,
                            state.selectedEndDateMillis!!,
                        )
                        viewModel.setDate(
                            formatEpochMilliseconds(
                                state.selectedStartDateMillis!!,
                                (state.selectedStartDateMillis!! + ((uiState.dateOptions.size - 1) * 24 * 60 * 60 * 1000)),
                            ),
                        )
                        onSendButtonClicked()
                    },
                    enabled = state.selectedEndDateMillis != null,
                ) {
                    Text(text = "Done", color = Color.Black)
                }
            }
            if (state.selectedEndDateMillis != null && state.selectedEndDateMillis!! > System.currentTimeMillis()) {
                state.setSelection(state.selectedStartDateMillis, System.currentTimeMillis())
            }
            DateRangePicker(
                state = state,
                title = {
                    Box(
                        modifier = Modifier.padding(
                            top = (screenwidth * 0.05).dp,
                            start = (screenwidth * 0.05).dp,
                            end = (screenwidth * 0.05).dp,
                            bottom = 0.dp,
                        ),
                    ) {
                        Text(text = "Select Start Date", fontSize = (screenwidth * 0.05).scaledSp())
                    }
                },
                headline = {
                    Box(
                        modifier = Modifier.padding(
                            top = 0.dp,
                            start = (screenwidth * 0.05).dp,
                            end = (screenwidth * 0.05).dp,
                            bottom = (screenwidth * 0.05).dp,
                        ),
                    ) {
                        if (state.selectedStartDateMillis != null && state.selectedEndDateMillis != null) {
                            Text(
                                text = formatHeadlineMilliseconds(
                                    state.selectedStartDateMillis,
                                    state.selectedEndDateMillis,
                                ),
                                fontSize = (screenwidth * 0.07).scaledSp(),
                            )
                        } else if (state.selectedStartDateMillis != null) {
                            Text(
                                text = formatHeadlineMillisecondsStart(state.selectedStartDateMillis),
                                fontSize = (screenwidth * 0.07).scaledSp(),
                            )
                        } else {
                            Text(text = "To - From", fontSize = (screenwidth * 0.07).scaledSp())
                        }
                    }
                },
                colors = DatePickerDefaults.colors(
                    dayInSelectionRangeContainerColor = Color(97, 153, 154).copy(alpha = 0.3f),
                    selectedDayContainerColor = Color(97, 153, 154),
                    selectedDayContentColor = Color.Black,
                    todayContentColor = Color.Black,
                    todayDateBorderColor = Color.Black,
                    dividerColor = Color(97, 153, 154),
                ),
            )
            if (state.selectedStartDateMillis != null && dayRange > 0) {
                state.setSelection(
                    state.selectedStartDateMillis,
                    addDaysToSelectedStartDate(state.selectedStartDateMillis!!, dayRange),
                )
            }
        }
    }
}

fun addDaysToSelectedStartDate(selectedStartDateMillis: Long, days: Long): Long {
    val secondsToAdd = days * 24 * 60 * 60
    val instant = Instant.ofEpochMilli(selectedStartDateMillis)
    val newStartDate = instant.plusSeconds(secondsToAdd)
    return newStartDate.toEpochMilli()
}

fun formatEpochMilliseconds(startEpochMilliseconds: Long, endEpochMilliseconds: Long): String {
    val startinstant = Instant.ofEpochMilli(startEpochMilliseconds)
    val startlocalDate = ZonedDateTime.ofInstant(startinstant, ZoneOffset.UTC).toLocalDate()
    val endinstant = Instant.ofEpochMilli(endEpochMilliseconds)
    val endlocalDate = ZonedDateTime.ofInstant(endinstant, ZoneOffset.UTC).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return startlocalDate.format(formatter) + "|" + endlocalDate.format(formatter)
}

fun formatHeadlineMilliseconds(startEpochMilliseconds: Long?, endEpochMilliseconds: Long?): String {
    val startinstant = startEpochMilliseconds?.let { Instant.ofEpochMilli(it) }
    val startlocalDate = ZonedDateTime.ofInstant(startinstant, ZoneOffset.UTC).toLocalDate()
    val endinstant = endEpochMilliseconds?.let { Instant.ofEpochMilli(it) }
    val endlocalDate = ZonedDateTime.ofInstant(endinstant, ZoneOffset.UTC).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("MMMM d")
    return startlocalDate.format(formatter) + " - " + endlocalDate.format(formatter)
}

fun formatHeadlineMillisecondsStart(startEpochMilliseconds: Long?): String {
    val startinstant = startEpochMilliseconds?.let { Instant.ofEpochMilli(it) }
    val startlocalDate = ZonedDateTime.ofInstant(startinstant, ZoneOffset.UTC).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("MMMM d")
    return startlocalDate.format(formatter) + " - To"
}

fun convertDateToEpochMillis(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDate = LocalDate.parse(dateString, formatter)
    val instant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
    return instant.toEpochMilli()
}

fun getDatesBetween(startEpochMilliseconds: Long, endEpochMilliseconds: Long): List<LocalDate> {
    val startDate =
        Instant.ofEpochMilli(startEpochMilliseconds).atZone(ZoneOffset.UTC).toLocalDate()
    val endDate = Instant.ofEpochMilli(endEpochMilliseconds).atZone(ZoneOffset.UTC).toLocalDate()
    val today = LocalDate.now()
    val dates = mutableListOf<LocalDate>()
    var currentDate = startDate
    while (!currentDate.isAfter(endDate)) {
        if (currentDate.isBefore(today) || currentDate == today) {
            dates.add(currentDate)
        }
        currentDate = currentDate.plusDays(1)
    }
    return dates
}
