package com.tpp.theperiodpurse.ui.cycle

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.entity.Date
import com.tpp.theperiodpurse.utility.findYears
import com.tpp.theperiodpurse.utility.parseDatesIntoPeriods
import com.tpp.theperiodpurse.ui.cycle.components.PeriodEntries
import com.tpp.theperiodpurse.ui.cycle.components.YearTab
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import java.time.LocalDate
import kotlin.collections.ArrayList

@Composable
fun PeriodHistoryLayout(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navController: NavController,
) {
    val dates = ArrayList(appViewModel.getDates())
    val bg = painterResource(appViewModel.colorPalette.background)
    val periods = parseDatesIntoPeriods(dates)
    var years = findYears(periods)
    if (years == null) {
        years = mutableMapOf()
        years[LocalDate.now().year] = ArrayList()
    }
    var yearSelected by remember { mutableStateOf(years.keys.last()) }
    val cyclePage = stringResource(R.string.cycle_page)
    // Iterate over years and create horizontally scrollable buttons
    Scaffold(
        topBar = {
            AppBar(navController = navController, appViewModel = appViewModel)
        },
        modifier = Modifier.safeDrawingPadding()
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Image(
                painter = bg,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = cyclePage },
                contentScale = ContentScale.FillBounds,
            )
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 25.dp),
            ) {
                LazyRow {
                    // Reverse keys
                    val reversedKeys = years.keys.reversed()
                    for (key in reversedKeys) {
                        item {
                            YearTab(key, yearSelected == key, onClick = {
                                yearSelected = key
                            })
                        }
                    }
                }
                PeriodCard(modifier, yearSelected, years, appViewModel)
            }
        }
    }
}

@Composable
private fun PeriodCard(
    modifier: Modifier,
    yearSelected: Int,
    years: MutableMap<Int, ArrayList<ArrayList<Date>>>,
    appViewModel: AppViewModel
) {
    Card(
        modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(50.0f),
    ) {
        Column(modifier.background(color = appViewModel.colorPalette.HeaderColor1).padding(horizontal = 15.dp, vertical = 15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    // Replace the year
                    text = yearSelected.toString(),
                    fontSize = 15.scaledSp(),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF868083),
                )
            }
            Divider(
                color = Color(0xFF868083),
                modifier = modifier
                    .padding(top = 5.dp, bottom = 10.dp)
                    .fillMaxWidth(),
            )
            // Show last three most recent periods
            years[yearSelected]?.let { it1 -> PeriodEntries(it1, null, appViewModel) }
        }
    }
}

@Composable
fun AppBar(navController: NavController, appViewModel: AppViewModel) {
    TopAppBar(
        {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(id = R.string.period_history),
                fontWeight = FontWeight.Bold,
                fontSize = 20.scaledSp(),
                color = appViewModel.colorPalette.MainFontColor
            )
        },
        backgroundColor = appViewModel.colorPalette.HeaderColor1,
        elevation = 0.dp,
        navigationIcon = { BackIcon(navController = navController, appViewModel = appViewModel) },
    )
}

@Composable
fun BackIcon(navController: NavController, appViewModel: AppViewModel) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button),
            tint = appViewModel.colorPalette.MainFontColor
        )
    }
}
