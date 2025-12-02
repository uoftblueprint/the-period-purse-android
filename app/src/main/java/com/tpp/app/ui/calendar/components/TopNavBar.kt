package com.tpp.app.ui.calendar.components

import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.tpp.app.ui.calendar.CalendarTabItem
import com.tpp.app.ui.calendar.noRippleClickable
import com.tpp.app.ui.theme.HeaderColor1
import com.tpp.app.ui.theme.SelectedColor1
import com.tpp.app.ui.viewmodel.AppViewModel
import com.tpp.app.ui.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch

private class NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(Color.Unspecified, true)

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleAlpha(draggedAlpha = 0f, focusedAlpha = 0f, hoveredAlpha = 0f, pressedAlpha = 0f)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<CalendarTabItem>, pagerState: PagerState, appViewModel: AppViewModel) {
    // Composable for tabs
    // Tabs are displayed in ordered row by tabs list
    // PagerState allows for navigation/animation between paginated layouts
    val scope = rememberCoroutineScope()
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = appViewModel.colorPalette.HeaderColor1,
            contentColor = appViewModel.colorPalette.MainFontColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions,
                    ),
                    color = SelectedColor1,
                )
            },
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(tab.title) },
                    selected = pagerState.currentPage == index,
                    modifier = Modifier
                        .noRippleClickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(
    tabs: List<CalendarTabItem>,
    pagerState: PagerState,
    calendarViewModel: CalendarViewModel,
    navController: NavController,
    appViewModel: AppViewModel,
) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen(
            navController = navController,
            calendarViewModel = calendarViewModel,
            appViewModel = appViewModel,
        )
    }
}

