package com.tpp.theperiodpurse.ui.symptomlog

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kizitonwose.calendar.core.atStartOfMonth
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.Screen
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.model.*
import com.tpp.theperiodpurse.ui.component.PopupTopBar
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.theme.SelectedColor1
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.CalendarViewModel
import com.tpp.theperiodpurse.ui.viewmodel.LogViewModel
import java.sql.Time
import java.time.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date as Date1

@Composable
fun LogScreen(
    date: String = "0001-01-01",
    navController: NavController,
    appViewModel: AppViewModel,
    calendarViewModel: CalendarViewModel,
    context: Context,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val day = LocalDate.parse(date)
        val appUiState by appViewModel.uiState.collectAsState()

        val logPrompts: MutableList<LogPrompt> = mutableListOf()

        for (symptom in appUiState.trackedSymptoms) {
            (LogPrompt from symptom.nameId)?.let { logPrompts.add(it) }
        }

        (LogPrompt from R.string.notes)?.let { logPrompts.add(it) }

        val logViewModel = LogViewModel(logPrompts)
        val calendarState by calendarViewModel.uiState.collectAsState()
        val dayUIState = calendarState.days[day]
        if (dayUIState != null) {
            logViewModel.populateWithUIState(dayUIState)
        }

        LogScreenLayout(
            day,
            navController,
            logPrompts,
            logViewModel,
            onSave = {
                navController.navigateUp()
                // If None is selected, it should not count is a square is filled
                var flow = logViewModel.getSelectedFlow()
                if (flow != null && flow.name == "None") {
                    flow = null
                }
                var cramps = logViewModel.getSelectedCrampSeverity()
                if (cramps != null && cramps.name == "None") {
                    cramps = null
                }
                var ovulation = logViewModel.getSelectedOvulation()
                if (ovulation != null && ovulation.name == "Not Ovulating") {
                    ovulation = null

                }
                calendarViewModel.setDayInfo(
                    day,
                    CalendarDayUIState(
                        flow = flow,
                        mood = logViewModel.getSelectedMood(),
                        exerciseLengthString = logViewModel.getText(LogPrompt.Exercise),
                        exerciseType = logViewModel.getSelectedExercise(),
                        crampSeverity = cramps,
                        sleepString = logViewModel.getText(LogPrompt.Sleep),
//                        ovulation = ovulation // Error
                    ),
                )
                if (logViewModel.isFilled()) {
                    var exercisedDuration: Duration? = null
                    val excTxt = logViewModel.getText(LogPrompt.Exercise)
                    if (excTxt != "") {
                        exercisedDuration = Duration.ofHours(
                            Time.valueOf(excTxt).hours.toLong(),
                        ).plusMinutes(
                            Time.valueOf(excTxt).minutes.toLong(),
                        )
                    }
                    var sleepDuration: Duration? = null
                    val sleepTxt = logViewModel.getText(LogPrompt.Sleep)
                    if (sleepTxt != "") {
                        sleepDuration = Duration.ofHours(
                            Time.valueOf(sleepTxt).hours.toLong(),
                        ).plusMinutes(
                            Time.valueOf(sleepTxt).minutes.toLong(),
                        )
                    }
                    Log.d("MOOD CHECK", logViewModel.getSquareSelected(LogPrompt.Mood).toString())
                    logViewModel.getSelectedMood()?.let { Log.d("MOOD CHECKS", it.displayName) }
                    appViewModel.saveDate(
                        Date(
                            date = Date1.from(
                                LocalDateTime.of(
                                    day,
                                    LocalDateTime.MIN.toLocalTime(),
                                ).atZone(ZoneId.systemDefault()).toInstant(),
                            ),
                            flow = logViewModel.getSquareSelected(LogPrompt.Flow)
                                ?.let { FlowSeverity.getSeverityByDisplayName(it) },
                            mood = logViewModel.getSelectedMood(),
                            exerciseLength = exercisedDuration,
                            exerciseType = logViewModel.getSquareSelected(LogPrompt.Exercise)
                                ?.let { Exercise.getExerciseByDisplayName(it) },
                            crampSeverity = logViewModel.getSquareSelected(LogPrompt.Cramps)
                                ?.let { CrampSeverity.getSeverityByDisplayName(it) },
                            sleep = sleepDuration,
                            notes = logViewModel.getText(LogPrompt.Notes),
                            ovulation = logViewModel.getSquareSelected(LogPrompt.Ovulation)
                        ),
                        context,
                    )
                } else {
                    // chcek  if given date is in the db, if it is, delete the entry
                    val dates = appViewModel.getDates()
                    for (d in dates) {
                        val thisDate = d.date?.toInstant()?.atZone(ZoneId.systemDefault())
                            ?.toLocalDate()
                        if (thisDate != null) {
                            if (thisDate == day) {
                                appViewModel.deleteDate(d, context)
                            }
                        }
                    }
                }
            },
            appViewModel = appViewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogScreenLayout(
    date: LocalDate,
    navController: NavController,
    logPrompts: List<LogPrompt>,
    logViewModel: LogViewModel,
    appViewModel: AppViewModel,
    onSave: () -> Unit,
) {
    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(appViewModel.colorPalette.HeaderColor1),
        ) {
            LogScreenTopBar(
                navController = navController,
                date = date,
                appViewModel = appViewModel
            )
            LogPromptCards(logPrompts = logPrompts, logViewModel = logViewModel, appViewModel = appViewModel)
            Spacer(
                modifier = Modifier
                    .height(130.dp)
                    .weight(1f),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )

            SaveButton(onClick = onSave)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogScreenTopBar(navController: NavController, date: LocalDate, appViewModel: AppViewModel) {
    PopupTopBar(onClose = { navController.navigateUp() }, appViewModel = appViewModel) {
        LogScreenTopBarContent(navController = navController, date = date, appViewModel = appViewModel)
    }
}

@Composable
private fun LogScreenTopBarContent(navController: NavController, date: LocalDate, appViewModel: AppViewModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 0.dp, start = 35.dp, end = 35.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(.1f),
        ) {
            if (date.minusDays(1) >=
                YearMonth.now().minusMonths(12).atStartOfMonth()
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(
                            "%s/%s/%s".format(
                                Screen.Calendar.name,
                                Screen.Log.name,
                                date.minusDays(1).toString(),
                            ),
                        ) {
                            popUpTo(Screen.Calendar.name) {
                                inclusive = false
                            }
                        }
                    },
                    modifier = Modifier,
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Log Back Arrow",
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(.8f),
        ) {
            val configuration = LocalConfiguration.current
            val screenwidth = configuration.screenWidthDp

            Text(
                text = "Log your symptoms for:",
                color = appViewModel.colorPalette.MainFontColor,
                fontSize = (screenwidth * 0.05).scaledSp(),
            )
            Text(
                text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 14.scaledSp(),
                modifier = Modifier
                    .testTag("DateLabel")
                    .semantics { contentDescription = "DateLabel" },
                color = appViewModel.colorPalette.MainFontColor
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(.1f),
        ) {
            if (date.plusDays(1) <= LocalDate.now()) {
                IconButton(
                    onClick = {
                        navController.navigate(
                            "%s/%s/%s"
                                .format(
                                    Screen.Calendar.name,
                                    Screen.Log.name,
                                    date.plusDays(1),
                                ),
                        ) {
                            popUpTo(Screen.Calendar.name) {
                                inclusive = false
                            }
                        }
                    },
                    modifier = Modifier
                        .semantics { contentDescription = "ClickNextDay" },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Log Forward Arrow",
                    )
                }
            }
        }
    }
}

@Composable
fun LogPromptCards(logPrompts: List<LogPrompt>, logViewModel: LogViewModel, appViewModel: AppViewModel) {
    LazyColumn(modifier = Modifier.background(appViewModel.colorPalette.HeaderColor1)) {
        itemsIndexed(logPrompts) { index, it ->
            Column() {
                LogPromptCard(logPrompt = it, logViewModel, appViewModel)
                if (index != logPrompts.lastIndex) {
                    Divider(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .background(color = appViewModel.colorPalette.lineColor))
                }
            }
        }
    }
}

@Composable
fun LogPromptCard(logPrompt: LogPrompt, logViewModel: LogViewModel, appViewModel: AppViewModel) {
    var expanded by remember { mutableStateOf(false) }

    val hasInput = (
        logViewModel.getSquareSelected(logPrompt) != null ||
            logViewModel.getText(logPrompt) != ""
        ) && logViewModel.getSquareSelected(logPrompt) != "Predicted"
    val iconColor = animateColorAsState(
        targetValue = if (hasInput) appViewModel.colorPalette.SelectedColor1 else  appViewModel.colorPalette.SecondaryFontColor,
        animationSpec = tween(500, 0, LinearEasing),
    )
    val tabColor = animateColorAsState(
        targetValue = if (hasInput)  appViewModel.colorPalette.SelectedColor1 else appViewModel.colorPalette.SecondaryFontColor,
        animationSpec = tween(500, 0, LinearEasing),
    )

    Column(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp, start = 30.dp, end = 15.dp),
            ) {
                logPrompt.icon(iconColor.value)
                Spacer(Modifier.size(10.dp))
                Text(
                    text = stringResource(logPrompt.title),
                    fontWeight = if (hasInput) FontWeight.ExtraBold else null,
                    color = tabColor.value,
                    fontSize = 16.scaledSp(),
                )
                Spacer(Modifier.weight(1f))
                ChangeableExpandButton(expanded = expanded) {
                    expanded = !expanded
                }
            }
        }
        if (expanded) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        bottom = 20.dp,
                        start = 40.dp,
                        end = 40.dp,
                    ),
            ) {
                logPrompt.prompt(logViewModel, appViewModel)
            }
        }
    }
}

@Composable
fun ChangeableExpandButton(expanded: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector =
            if (expanded) {
                Icons.Filled.KeyboardArrowUp
            } else Icons.Filled.KeyboardArrowDown,
            tint = SelectedColor1,
            contentDescription = "Expand Button",
        )
    }
}

@Composable
fun LogSelectableSquare(
    logSquare: LogSquare,
    selected: String?,
    appViewModel: AppViewModel,
    onClick: (LogSquare) -> Unit,
) {
    val squareColor = animateColorAsState(
        targetValue =
        if (logSquare.description == selected) {
            appViewModel.colorPalette.SelectedColor1
        } else {
            appViewModel.colorPalette.secondary4
        },
        animationSpec = tween(250, 0, LinearEasing),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize(Alignment.Center),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(75.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                    color = squareColor.value,
                )
                .clickable {
                    onClick(logSquare)
                }
                .semantics { contentDescription = logSquare.description },
        ) {
            logSquare.icon(appViewModel.colorPalette.MainFontColor)
        }
        Text(
            text = logSquare.description,
            fontSize = 16.sp,
            color = appViewModel.colorPalette.SecondaryFontColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, bottom = 20.dp),
        )
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier,
    ) {
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = SelectedColor1,
            modifier = Modifier
                .padding(20.dp)
                .width(350.dp)
                .height(40.dp),
        ) {
            Text(
                text = "Save",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .semantics { contentDescription = "Save Button" },
                color = Color.Black,
            )
        }
    }
}


@Preview
@Composable
fun SaveButtonPreview() {
    SaveButton() {}
}
