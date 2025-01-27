package com.tpp.theperiodpurse.ui.onboarding

import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.OnboardingScreen
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.state.OnboardUIState
import com.tpp.theperiodpurse.ui.theme.ButtonDisabledColor
import com.tpp.theperiodpurse.ui.theme.Teal

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuestionThreeScreen(
    onSelectionChanged: (String) -> Unit = {},
    canNavigateBack: Boolean,
    navController: NavHostController,
    onboardUiState: OnboardUIState,
) {
    var selectedValue by rememberSaveable { mutableStateOf("") }
    var updateList by rememberSaveable { mutableStateOf(false) }
    if (!updateList && onboardUiState.symptomsOptions.isNotEmpty()) {
        onboardUiState.symptomsOptions.forEach { option ->
            if (option != "" && !selectedValue.contains(option)) {
                selectedValue = "$selectedValue$option|"
            }
        }
        updateList = true
    }
    val configuration = LocalConfiguration.current
    val screenwidth = configuration.screenWidthDp
    val screenheight = configuration.screenHeightDp
    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        val ratio = 0.45
        val ratioimage = 0.17
        val height = (screenheight * ratio)
        val imageheight = (screenheight * ratioimage)
        Box(
            modifier = Modifier
                .width(screenwidth.dp)
                .height(height.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.background_shape__1_),
                contentDescription = null,
                modifier = Modifier.size((screenheight * 0.65).dp),
            )
            Image(
                painter = painterResource(R.drawable.menstruation_calendar__1_),
                contentDescription = null,
                modifier = Modifier
                    .size(imageheight.dp)
                    .align(Alignment.Center),
            )
            Image(
                painter = painterResource(R.drawable.onboard_bar3),
                contentDescription = null,
                modifier = Modifier
                    .size((screenheight * (0.09)).dp)
                    .align(Alignment.BottomCenter),
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy((screenheight * (0.0025)).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height((screenheight * (0.40)).dp))
            Text(
                text = stringResource(R.string.question_three),
                fontSize = (screenheight * (0.035)).scaledSp(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width((screenheight * (0.7)).dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.description_three),
                fontSize = (screenheight * (0.03)).scaledSp(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height((screenheight * (0.005)).dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = (screenheight * (0.02)).dp)
                    .height((screenheight * (0.15)).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15))
                    .background(Color(rgb(188, 235, 214))),
            ) {
                Row(
                    modifier = Modifier.padding(
                        top = (screenheight * (0.02)).dp,
                        start = (screenheight * (0.03)).dp,
                        end = (screenheight * (0.03)).dp,
                    ),
                    horizontalArrangement = Arrangement.spacedBy((screenheight * (0.04)).dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .height((screenheight * (0.08)).dp)
                                .width((screenheight * (0.08)).dp)
                                .clip(RoundedCornerShape(30))
                                .background(Color(rgb(220, 242, 240))),
                        ) {
                            Image(
                                painter = painterResource(R.drawable.opacity_black_24dp),
                                contentDescription = null,
                                modifier = Modifier
                                    .size((screenheight * (0.05)).dp)
                                    .align(Alignment.Center),
                                colorFilter = ColorFilter.tint(Color.Gray),
                            )
                        }
                        Text(
                            text = stringResource(R.string.flow),
                            fontSize = 12.scaledSp(),
                            color = Color.Gray,
                        )
                    }
                    Text(
                        text = stringResource(R.string.caption_three),
                        fontSize = 13.scaledSp(),
                    )
                }
            }
            Spacer(modifier = Modifier.height((screenheight * (0.04)).dp))
            Row() {
                Column(
                    modifier = Modifier
                        .selectable(selected = selectedValue.contains("Mood"), onClick = {
                            val item = "Mood"
                            if (selectedValue.contains(item)) {
                                selectedValue = selectedValue.replace(item, "")
                            } else if (!selectedValue.contains(item)) {
                                selectedValue = selectedValue + "|" + item
                            }
                        })
                        .padding(start = (screenheight * 0.02).dp, end = 8.dp)
                        .semantics { contentDescription = "Mood" },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .height((screenheight * (0.07)).dp)
                            .width((screenheight * (0.07)).dp)
                            .clip(RoundedCornerShape(30))
                            .then(
                                if (!selectedValue.contains("Mood")) {
                                    Modifier.background(Color.White)
                                } else Modifier.background(
                                    Color(rgb(142, 212, 193)),
                                ),
                            ),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sentiment_neutral_black_24dp),
                            contentDescription = "Mood",
                            modifier = Modifier
                                .size((screenheight * (0.04)).dp)
                                .align(Alignment.Center),
                        )
                    }
                    Text(
                        text = stringResource(R.string.mood),
                        fontSize = 13.scaledSp(),
                    )
                }
                Column(
                    modifier = Modifier
                        .selectable(selected = selectedValue.contains("Exercise"), onClick = {
                            val item = "Exercise"
                            if (selectedValue.contains(item)) {
                                selectedValue = selectedValue.replace(item, "")
                            } else if (!selectedValue.contains(item)) {
                                selectedValue = selectedValue + "|" + item
                            }
                        })
                        .padding(horizontal = 8.dp)
                        .semantics { contentDescription = "fitness" },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .height((screenheight * (0.07)).dp)
                            .width((screenheight * (0.07)).dp)
                            .clip(RoundedCornerShape(30))
                            .then(
                                if (!selectedValue.contains("Exercise")) {
                                    Modifier.background(Color.White)
                                } else Modifier.background(
                                    Color(rgb(142, 212, 193)),
                                ),
                            ),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.self_improvement_black_24dp),
                            contentDescription = "Exercise",
                            modifier = Modifier
                                .size((screenheight * (0.04)).dp)
                                .align(Alignment.Center),
                        )
                    }
                    Text(
                        text = stringResource(R.string.exercise),
                        fontSize = 13.scaledSp(),
                    )
                }
                Column(
                    modifier = Modifier
                        .selectable(selected = selectedValue.contains("Cramps"), onClick = {
                            val item = "Cramps"
                            if (selectedValue.contains(item)) {
                                selectedValue = selectedValue.replace(item, "")
                            } else if (!selectedValue.contains(item)) {
                                selectedValue = selectedValue + "|" + item
                            }
                        })
                        .padding(horizontal = 8.dp)
                        .semantics { contentDescription = "Cramps" },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .height((screenheight * (0.07)).dp)
                            .width((screenheight * (0.07)).dp)
                            .clip(RoundedCornerShape(30))
                            .then(
                                if (!selectedValue.contains("Cramps")) {
                                    Modifier.background(Color.White)
                                } else Modifier.background(
                                    Color(rgb(142, 212, 193)),
                                ),
                            ),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sick_black_24dp),
                            contentDescription = "Cramps",
                            modifier = Modifier
                                .size((screenheight * (0.04)).dp)
                                .align(Alignment.Center),
                        )
                    }
                    Text(
                        text = stringResource(R.string.cramps),
                        fontSize = 13.scaledSp(),
                    )
                }
                Column(
                    modifier = Modifier
                        .selectable(selected = selectedValue.contains("Sleep"), onClick = {
                            val item = "Sleep"
                            if (selectedValue.contains(item)) {
                                selectedValue = selectedValue.replace(item, "")
                            } else if (!selectedValue.contains(item)) {
                                selectedValue = selectedValue + "|" + item
                            }
                        })
                        .padding(horizontal = 8.dp)
                        .semantics { contentDescription = "Sleep" },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .height((screenheight * (0.07)).dp)
                            .width((screenheight * (0.07)).dp)
                            .clip(RoundedCornerShape(30))
                            .semantics { contentDescription = "Sleep" }
                            .then(
                                if (!selectedValue.contains("Sleep")) {
                                    Modifier.background(Color.White)
                                } else Modifier.background(
                                    Color(rgb(142, 212, 193)),
                                ),
                            ),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.nightlight_black_24dp),
                            contentDescription = "Sleep",
                            modifier = Modifier
                                .size((screenheight * (0.04)).dp)
                                .align(Alignment.Center),
                        )
                    }
                    Text(
                        text = stringResource(R.string.sleep),
                        fontSize = 13.scaledSp(),
                    )
                }
                Column(
                    modifier = Modifier
                        .selectable(selected = selectedValue.contains("Ovulation"), onClick = {
                            val item = "Ovulation"
                            if (selectedValue.contains(item)) {
                                selectedValue = selectedValue.replace(item, "")
                            } else if (!selectedValue.contains(item)) {
                                selectedValue = selectedValue + "|" + item
                            }
                        })
                        .padding(start = 8.dp, end = (screenheight * 0.02).dp)
                        .semantics { contentDescription = "Ovulation" },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .height((screenheight * (0.07)).dp)
                            .width((screenheight * (0.07)).dp)
                            .clip(RoundedCornerShape(30))
                            .semantics { contentDescription = "Ovulation" }
                            .then(
                                if (!selectedValue.contains("Ovulation")) {
                                    Modifier.background(Color.White)
                                } else Modifier.background(
                                    Color(rgb(142, 212, 193)),
                                ),
                            ),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ovulation_egg_24dp),
                            contentDescription = "Ovulation",
                            modifier = Modifier
                                .size((screenheight * (0.04)).dp)
                                .align(Alignment.Center),
                        )
                    }
                    Text(
                        text = stringResource(R.string.ovulation),
                        fontSize = 13.scaledSp(),
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = (screenheight * (0.005)).dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TextButton(
                modifier = Modifier
                    .padding(start = (screenwidth * (0.1)).dp)
                    .weight(1f)
                    .semantics { contentDescription = "Skip" },
                onClick = {
                    onSelectionChanged("")
                    navController.navigate(OnboardingScreen.Summary.name)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            ) {
                Text(
                    stringResource(R.string.skip),
                    color = Color.Black,
                    fontSize = 20.scaledSp(),
                )
            }
            Button(
                modifier = Modifier
                    .padding(end = (screenwidth * (0.1)).dp)
                    .weight(1f)
                    .semantics { contentDescription = "Next" },
                // the button is enabled when the user makes a selection
                enabled = selectedValue.replace("|", "").isNotEmpty(),
                onClick = {
                    onSelectionChanged(selectedValue)
                    navController.navigate(OnboardingScreen.Summary.name)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Teal,
                    disabledBackgroundColor = ButtonDisabledColor,
                ),
            ) {
                Text(stringResource(R.string.next), color = Color.White, fontSize = 20.scaledSp())
            }
        }
    }
    backbutton({
        navController.navigateUp()
        onSelectionChanged(selectedValue)
    }, canNavigateBack)
}
