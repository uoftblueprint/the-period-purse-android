package com.tpp.theperiodpurse.ui.cycle

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.Screen
import com.tpp.theperiodpurse.data.model.Symptom
import com.tpp.theperiodpurse.ui.cycle.components.AverageLengthBox
import com.tpp.theperiodpurse.ui.cycle.components.CurrentCycleBox
import com.tpp.theperiodpurse.ui.cycle.components.CycleHistoryBox
import com.tpp.theperiodpurse.ui.cycle.components.UpcomingPeriodBox
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.utility.calculateAverageCycleLength
import com.tpp.theperiodpurse.utility.calculateAveragePeriodLength
import com.tpp.theperiodpurse.utility.calculateAverageOvulationLength
import com.tpp.theperiodpurse.utility.calculateDaysSinceLastPeriod
import kotlin.collections.ArrayList
import kotlin.math.max

private var periodLength = (-1).toFloat()
private var cycleLength = (-1).toFloat()
private var ovulationLength = (-1).toFloat()

@Composable
fun CycleScreenLayout(
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val dates = ArrayList(appViewModel.getDates())
    val bg = painterResource(appViewModel.colorPalette.background)

    periodLength = calculateAveragePeriodLength(dates)
    cycleLength = calculateAverageCycleLength(dates)
    ovulationLength = calculateAverageOvulationLength(dates)

    val daysUntilNextPeriod = max((cycleLength - calculateDaysSinceLastPeriod(dates)).toInt(), 0)

    Box {
        Image(
            painter = bg,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = "Cycle Page" },
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
                .padding(horizontal = 20.dp, vertical = 25.dp),
        ) {
            // if next predicted <= 7 days
            if (daysUntilNextPeriod <= 7) {
                if (daysUntilNextPeriod <= 0) {
                    UpcomingPeriodBox("You period will likely come any day now!",
                        appViewModel = appViewModel)
                } else {
                    UpcomingPeriodBox("You period might be coming in the next " +
                        "$daysUntilNextPeriod days",
                        appViewModel = appViewModel)
                }
            }
            Spacer(modifier.height(15.dp))
            CurrentCycleBox(dates = dates, appViewModel = appViewModel)
            Spacer(modifier.height(30.dp))
            Row {
                AverageLengthBox(
                    title = stringResource(R.string.avg_period_len),
                    color = appViewModel.colorPalette.cyclePink,
                    length = periodLength,
                    image = painterResource(R.drawable.flow_with_heart),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier.width(16.dp))
                AverageLengthBox(
                    title = stringResource(R.string.avg_cycle_len),
                    color = appViewModel.colorPalette.cycleBlue,
                    length = cycleLength,
                    image = painterResource(R.drawable.menstruation_calendar__1_),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Check if "Ovulation" is being tracked
                if (appViewModel.getTrackedSymptoms().any { it.name == Symptom.OVULATION.name }) {
                    AverageLengthBox(
                        title = stringResource(R.string.avg_ovulation_len),
                        color = appViewModel.colorPalette.cycleBlue,
                        length = ovulationLength,
                        image = painterResource(R.drawable.ovulation_egg_24dp),
                        modifier = Modifier.weight(1f) // Takes up half the width
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Mimics the second box to take up half the width
                }
            }
            Spacer(modifier.height(30.dp))
            CycleHistoryBox(
                dates = dates,
                onClickShowFull = {
                    navController.navigate(Screen.CycleFullHistory.name)
                },
                appViewModel = appViewModel
            )
            Spacer(modifier.height(80.dp))
        }
    }
}
