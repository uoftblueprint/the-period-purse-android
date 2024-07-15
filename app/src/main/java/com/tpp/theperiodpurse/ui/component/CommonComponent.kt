package com.tpp.theperiodpurse.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.Screen
import com.tpp.theperiodpurse.ui.theme.Red
import com.tpp.theperiodpurse.ui.theme.Teal
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

@Composable
fun BottomNavigation(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    navItemModifier: Modifier = Modifier,
) {
    appViewModel.uiState.collectAsState().value.darkMode
    Column(
        modifier = Modifier.safeDrawingPadding(),
    ) {
        Box {
            BottomNavigation(
                backgroundColor = appViewModel.colorPalette.HeaderColor1,
                modifier = modifier.align(Alignment.BottomCenter),
                elevation = 0.dp
            ) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.info_black_24dp),
                            tint = appViewModel.colorPalette.MainFontColor,
                            contentDescription = null,
                        )
                    },
                    label = { Text(text = Screen.Learn.name,  color = appViewModel.colorPalette.MainFontColor) },
                    selected = false,
                    onClick = { navController.navigate(Screen.Learn.name) },
                    modifier = navItemModifier,
                )

                Spacer(modifier = modifier.width(58.dp))

                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(R.drawable.settings_black_24dp),
                            tint = appViewModel.colorPalette.MainFontColor,
                            contentDescription = null,
                        )
                    },
                    label = { Text(text = Screen.Settings.name,
                    color = appViewModel.colorPalette.MainFontColor) },
                    selected = false,
                    onClick = { navController.navigate(Screen.Settings.name) },
                    modifier = navItemModifier,
                )
            }
        }
    }
}

@Composable
fun FloatingActionButton(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    contentColor: Color = Color.White,
    onClickInCalendar: () -> Unit,
) {
    val circle = MaterialTheme.shapes.large.copy(CornerSize(percent = 50))
    var iconId by remember { mutableStateOf(R.drawable.add_black_24dp) }
    var backgroundColor by remember { mutableStateOf(Red) }
    var contentId by remember { mutableStateOf(R.string.fab_to_calendar) }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        if (destination.route == Screen.Calendar.name) {
            iconId = R.drawable.add_black_24dp
            backgroundColor = Red
            contentId = R.string.fab_see_log_options
        } else {
            iconId = R.drawable.today_black_24dp
            backgroundColor = Teal
            contentId = R.string.fab_to_calendar
        }
    }
    FloatingActionButton(
        onClick =
        {
            if (navController.currentDestination?.route == Screen.Calendar.name) {
                onClickInCalendar()
            } else {
                navController.navigate(Screen.Calendar.name) {
                }
            }
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        modifier = modifier
            .padding(14.dp)
            .border(color = Color.White, width = 2.dp, shape = circle)
            .size(70.dp),
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(contentId),
            modifier = Modifier
                .width(30.dp)
                .aspectRatio(1f),
        )
    }
}

@Composable
fun PopupTopBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    content:
    @Composable()
    () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(color = appViewModel.colorPalette.popUpTopBar)
            .height(100.dp)
            .padding(top = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 10.dp),
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Button",
                )
            }
        }
        content()
    }
}

@Composable
fun Background () {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        )
}
