package com.tpp.theperiodpurse.ui.onboarding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.*
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.data.model.FlowSeverity
import com.tpp.theperiodpurse.data.model.Symptom
import com.tpp.theperiodpurse.ui.component.Background
import com.tpp.theperiodpurse.ui.state.CalendarDayUIState
import com.tpp.theperiodpurse.ui.state.OnboardUIState
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.CalendarViewModel
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * This composable expects [onboardUIState] that represents the onboarding state, [onCancelButtonClicked] lambda
 * that triggers canceling the order and passes the final order to [onSendButtonClicked] lambda
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryScreen(
    onboardUiState: OnboardUIState,
    onSendButtonClicked: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBack: Boolean,
    viewModel: OnboardViewModel,
    appViewModel: AppViewModel,
    calendarViewModel: CalendarViewModel,
) {
    val context = LocalContext.current.applicationContext
    val resources = LocalContext.current.resources
    val numberOfDays = resources.getQuantityString(
        R.plurals.period_length,
        onboardUiState.days,
        onboardUiState.days,
    )
    val configuration = LocalConfiguration.current
    val screenheight = configuration.screenHeightDp
    Background()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.safeDrawingPadding()
    ) {
        Backbutton(navigateUp, canNavigateBack)
        Spacer(modifier = Modifier.height((screenheight * (0.12)).dp))
        Image(
            painter = painterResource(R.drawable.pad_3_2x),
            contentDescription = null,
            modifier = Modifier.size((screenheight * 0.25).dp),
        )
        Text(
            text = "You're all set!",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            fontSize = 33.scaledSp(),
        )
        Spacer(modifier = Modifier.height((screenheight * 0.08).dp))
        if (onboardUiState.days != 0) {
            Column(Modifier.padding(start = 25.dp)) {
                Text(
                    text = stringResource(R.string.average_period_length),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(97, 153, 154),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.scaledSp(),
                )
                Text(
                    text = numberOfDays,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    fontSize = 17.scaledSp(),
                )
            }
            Divider(
                thickness = 1.dp,
                color = Color(97, 153, 154),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (!onboardUiState.date.contains("Choose date")) {
            Column(Modifier.padding(start = 25.dp)) {
                Text(
                    stringResource(R.string.last_period),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(97, 153, 154),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.scaledSp(),
                )
                Text(
                    text = onboardUiState.date,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    fontSize = 17.scaledSp(),
                )
            }
            Divider(
                thickness = 1.dp,
                color = Color(97, 153, 154),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Column(Modifier.padding(start = 25.dp)) {
            Text(
                stringResource(R.string.symptoms_to_log),
                modifier = Modifier.fillMaxWidth(),
                color = Color(97, 153, 154),
                fontWeight = FontWeight.Bold,
                fontSize = 15.scaledSp(),
            )
            val symptoms = listOf(
                "Mood",
                "Exercise",
                "Cramps",
                "Sleep",
            )
            Row() {
                var dontprint = false
                Column(Modifier.padding(top = 10.dp, bottom = 10.dp, end = 5.dp)) {
                    Image(
                        painter = painterResource(R.drawable.opacity_black_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                    )
                }
                symptoms.forEach { symptom ->
                    onboardUiState.symptomsOptions.forEach { select ->
                        if (symptom == select || symptom == "Flow") {
                            Column(
                                modifier = Modifier
                                    .padding(all = 10.dp)
                                    .semantics { contentDescription = symptom },
                            ) {
                                var painter =
                                    painterResource(R.drawable.self_improvement_black_24dp)
                                when (symptom) {
                                    "Mood" ->
                                        painter =
                                            painterResource(R.drawable.sentiment_neutral_black_24dp)

                                    "Exercise" ->
                                        painter =
                                            painterResource(R.drawable.self_improvement_black_24dp)

                                    "Cramps" ->
                                        painter =
                                            painterResource(R.drawable.sick_black_24dp)

                                    "Sleep" ->
                                        painter =
                                            painterResource(R.drawable.nightlight_black_24dp)

                                    else -> { // Note the block
                                        dontprint = true
                                    }
                                }
                                if (!dontprint) {
                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Divider(thickness = 1.dp, color = Color(97, 153, 154), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height((screenheight * 0.05).dp))
        Button(
            modifier = Modifier
                .padding(horizontal = (screenheight * 0.02).dp)
                .fillMaxWidth(),
            onClick = {
                viewModel.addNewUser(
                    getSymptomsToTrack(onboardUiState.symptomsOptions),
                    arrayListOf<Date>(),
                    onboardUiState.days,
                    0,
                    getDaysSince(onboardUiState.date),
                    context = context,
                )
                onboardUiState.dateOptions.forEach {
                    appViewModel.saveDate(
                        Date(
                            date = java.util.Date.from(
                                it.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                            ),
                            flow = FlowSeverity.Medium,
                            exerciseType = null,
                            exerciseLength = null,
                            crampSeverity = null,
                            sleep = null,
                            mood = null,
                            notes = "",
                        ),
                        context,
                    )
                    calendarViewModel.updateDayInfo(
                        it,
                        CalendarDayUIState(flow = FlowSeverity.Medium),
                    )
                }
                onSendButtonClicked()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(97, 153, 154)),
        ) {
            Text(stringResource(R.string.lets_go), color = Color.White, fontSize = 25.scaledSp())
        }
    }
}

fun getDaysSince(date: String): Int {
    if (!date.contains("Choose date")) {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = date.split(" to ")[0]
        val from = LocalDate.parse(date, dateFormatter)
        val today = LocalDate.now()
        return ChronoUnit.DAYS.between(today, from).toInt()
    }
    return 0
}

fun getSymptomsToTrack(strList: List<String>): ArrayList<Symptom> {
    val list_so_far = arrayListOf<Symptom>()
    strList.forEach { symptom ->
        when (symptom) {
            "Mood" -> {
                list_so_far.add(Symptom.MOOD)
            }

            "Exercise" -> {
                list_so_far.add(Symptom.EXERCISE)
            }

            "Flow" -> {
                list_so_far.add(Symptom.FLOW)
            }

            "Cramps" -> {
                list_so_far.add(Symptom.CRAMPS)
            }

            "Sleep" -> {
                list_so_far.add(Symptom.SLEEP)
            }
        }
    }
    return list_so_far
}
