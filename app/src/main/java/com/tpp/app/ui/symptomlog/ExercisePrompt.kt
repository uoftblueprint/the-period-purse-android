package com.theperiodpurse.app.ui.symptomlog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theperiodpurse.app.R
import com.theperiodpurse.app.data.model.LogPrompt
import com.theperiodpurse.app.data.model.LogSquare
import com.theperiodpurse.app.ui.onboarding.EditNumberField
import com.theperiodpurse.app.ui.viewmodel.AppViewModel
import com.theperiodpurse.app.ui.viewmodel.LogViewModel
import java.sql.Time

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExercisePrompt(logViewModel: LogViewModel, appViewModel: AppViewModel) {
    val selectedTime = logViewModel.getText(LogPrompt.Exercise)
    var hoursExercised by remember {
        mutableStateOf(
            if (selectedTime != "") {
                Time.valueOf(selectedTime).hours.toString()
            } else {
                ""
            },
        )
    }
    var minutesExercised by remember {
        mutableStateOf(
            if (selectedTime != "") {
                Time.valueOf(selectedTime).minutes.toString()
            } else {
                ""
            },
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier // this box allows clicking out of the keyboard
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                keyboardController?.hide()

                focusManager.clearFocus(true)
                try {
                    saveTextData(logViewModel, hoursExercised, minutesExercised)
                } catch (e: NumberFormatException) {
                    hoursExercised = ""
                    minutesExercised = ""
                    saveTextData(logViewModel, hoursExercised, minutesExercised)
                }
            },
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(85.dp),
                ) {
                    EditNumberField(
                        appViewModel = appViewModel,
                        label = R.string.hours,
                        value = hoursExercised,
                        onValueChange = {
                            hoursExercised = it
                            try {
                                saveTextData(logViewModel, hoursExercised, minutesExercised)
                            } catch (e: NumberFormatException) {
                                hoursExercised = ""
                                minutesExercised = ""
                                saveTextData(logViewModel, hoursExercised, minutesExercised)
                            }
                        },

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.moveFocus(FocusDirection.Right)
                                saveTextData(logViewModel, hoursExercised, minutesExercised)
                            },
                        ),
                    )
                }

                Spacer(modifier = Modifier.size(17.dp))
                Box(
                    modifier = Modifier
                        .width(100.dp),
                ) {
                    EditNumberField(
                        appViewModel = appViewModel,
                        label = R.string.minutes,
                        value = minutesExercised,
                        onValueChange = {
                            minutesExercised = it
                            try {
                                saveTextData(logViewModel, hoursExercised, minutesExercised)
                            } catch (e: NumberFormatException) {
                                hoursExercised = ""
                                minutesExercised = ""
                                saveTextData(logViewModel, hoursExercised, minutesExercised)
                            }
                        },

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                saveTextData(logViewModel, hoursExercised, minutesExercised)
                            },
                        ),
                    )
                }
                Spacer(modifier = Modifier.size(6.dp))
                Button(
                    onClick = {
                        logViewModel.resetText(LogPrompt.Exercise)
                        hoursExercised = ""
                        minutesExercised = ""
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = appViewModel
                        .colorPalette.secondary1),
                    modifier = Modifier
                        .padding(start = 16.dp),
                ) {
                    Text(
                        "Clear",
                    )
                }
            }
            var selected by remember {
                mutableStateOf(logViewModel.getSquareSelected(logPrompt = LogPrompt.Exercise))
            }

            val flowSquares = listOf(
                LogSquare.ExerciseCardio,
                LogSquare.ExerciseYoga,
                LogSquare.ExerciseStrength,
                LogSquare.ExerciseBallSports,
                LogSquare.ExerciseMartialArts,
                LogSquare.ExerciseWaterSports,
                LogSquare.ExerciseCycling,
                LogSquare.ExerciseRacquetSports,
                LogSquare.ExerciseWinterSports,
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    start = 0.dp,
                    top = 20.dp,
                    end = 0.dp,
                    bottom = 16.dp,
                ),
                modifier = Modifier
                    .height(400.dp),
                content = {
                    items(flowSquares) { flowSquare ->
                        LogSelectableSquare(
                            logSquare = flowSquare,
                            selected = selected,
                            appViewModel = appViewModel
                        ) { logSquare ->
                            if (selected == logSquare.description) {
                                selected = null
                                logViewModel.resetSquareSelected(logSquare)
                            } else {
                                selected = logSquare.description
                                logViewModel.setSquareSelected(logSquare)
                            }
                        }
                    }
                },
            )
        }
    }
}

private fun saveTextData(
    logViewModel: LogViewModel,
    hoursExercised: String,
    minutesExercised: String,
) {
    val time = Time(
        if (hoursExercised != "") hoursExercised.toInt() else 0,
        if (minutesExercised != "") minutesExercised.toInt() else 0,
        0,
    )
    logViewModel.setText(LogPrompt.Exercise, time.toString())
}
