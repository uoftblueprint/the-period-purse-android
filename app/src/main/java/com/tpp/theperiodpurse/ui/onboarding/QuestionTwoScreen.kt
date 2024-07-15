package com.tpp.theperiodpurse.ui.onboarding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.tpp.theperiodpurse.ui.component.Background
import com.tpp.theperiodpurse.ui.state.OnboardUIState
import com.tpp.theperiodpurse.ui.theme.ButtonDisabledColor
import com.tpp.theperiodpurse.ui.theme.Teal

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuestionTwoScreen(
    modifier: Modifier = Modifier,
    onboardUiState: OnboardUIState,
    onSelectionChanged: (String) -> Unit = {},
    navigateUp: () -> Unit,
    canNavigateBack: Boolean,
    navController: NavHostController,
) {
    var entered by rememberSaveable { mutableStateOf(false) }
    val mDate = rememberSaveable { mutableStateOf("Choose date") }
    val mDateTo = rememberSaveable { mutableStateOf("") }
    if (onboardUiState.date.contains("/")) {
        mDate.value = onboardUiState.date.split(" to ")[0]
        mDateTo.value = onboardUiState.date.split(" to ")[1]
        entered = true
    }
    val configuration = LocalConfiguration.current
    val screenwidth = configuration.screenWidthDp
    val screenheight = configuration.screenHeightDp
    Background()
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .safeDrawingPadding()
    ) {
        Backbutton(navigateUp, canNavigateBack)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val ratio = 0.5
            val ratioimage = 0.17
            val height = (screenheight * ratio)
            val imageheight = (screenheight * ratioimage)
            Box(
                modifier = Modifier
                    .width(screenwidth.dp)
                    .height(height.dp),
            ) {
                Background_shape()
                Image(
                    painter = painterResource(R.drawable.flow_with_heart),
                    contentDescription = null,
                    modifier = Modifier
                        .size(imageheight.dp)
                        .align(Alignment.Center),
                )
                val barheight1 = (screenheight * (0.08))
                Image(
                    painter = painterResource(R.drawable.onboard_bar2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(barheight1.dp)
                        .align(Alignment.BottomCenter),
                )
            }
            Text(
                text = stringResource(R.string.question_two),
                fontSize = (screenheight * (0.035)).scaledSp(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(250.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.description_two),
                fontSize = (screenheight * (0.03)).scaledSp(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width((screenwidth * (0.7)).dp),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height((screenheight * (0.005)).dp))
            Button(
                onClick = {
                    navController.navigate(OnboardingScreen.DateRangePicker.name)
                    entered = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                modifier = modifier
                    .width(175.dp)
                    .semantics { contentDescription = "datepick" },
                shape = RoundedCornerShape(20),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                ),
            ) {
                if (mDate.value.contains("Choose date")) {
                    Row(
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 16.dp,
                            top = 12.dp,
                            bottom = 12.dp,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(text = mDate.value, color = Color.Gray, fontSize = 14.scaledSp())
                        Image(
                            painter = painterResource(R.drawable.onboard_calendar),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 10.dp, end = 0.dp),
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 12.dp,
                            bottom = 12.dp,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(text = mDate.value, color = Color.Black, fontSize = 17.scaledSp())
                    }
                }
            }
            Spacer(Modifier.height((screenwidth * (0.01)).dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = (screenheight * (0.02)).dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TextButton(
                onClick = {
                    onboardUiState.date = "Choose date"
                    onboardUiState.dateOptions = listOf()
                    navController.navigate(OnboardingScreen.QuestionThree.name)
                },
                modifier = modifier
                    .padding(start = (screenwidth * (0.1)).dp)
                    .weight(1f)
                    .semantics { contentDescription = "Skip" },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),

            ) {
                Text(
                    stringResource(R.string.skip),
                    color = Color.Black,
                    fontSize = 20.scaledSp(),
                )
            }
            Button(
                onClick = {
                    onSelectionChanged(mDate.value + "|" + mDateTo.value)
                    navController.navigate(OnboardingScreen.QuestionThree.name)
                },
                enabled = entered,
                modifier = modifier
                    .padding(end = (screenwidth * (0.1)).dp)
                    .weight(1f)
                    .semantics { contentDescription = "Next" },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Teal,
                    disabledBackgroundColor = ButtonDisabledColor,
                ),
            ) {
                Text(stringResource(R.string.next), color = Color.White, fontSize = 20.scaledSp())
            }
        }
    }
}
