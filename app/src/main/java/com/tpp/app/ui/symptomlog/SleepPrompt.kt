package com.tpp.app.ui.symptomlog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tpp.app.R
import com.tpp.app.data.model.LogPrompt
import com.tpp.app.ui.onboarding.EditNumberField
import com.tpp.app.ui.viewmodel.AppViewModel
import com.tpp.app.ui.viewmodel.LogViewModel
import java.sql.Time

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SleepPrompt(logViewModel: LogViewModel, appViewModel: AppViewModel) {
    val selectedTime = logViewModel.getText(LogPrompt.Sleep)
    var hoursSlept by remember {
        mutableStateOf(
            if (selectedTime != "") {
                Time.valueOf(selectedTime).hours.toString()
            } else {
                ""
            },
        )
    }
    var minutesSlept by remember {
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
                saveTextData(logViewModel, hoursSlept, minutesSlept)
                focusManager.clearFocus(true)
            },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    bottom = 10.dp,
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(85.dp),
            ) {
                EditNumberField(
                    appViewModel = appViewModel,
                    label = R.string.hours,
                    value = hoursSlept,
                    onValueChange = {
                        hoursSlept = it
                        try {
                            saveTextData(logViewModel, hoursSlept, minutesSlept)
                        } catch (e: NumberFormatException) {
                            hoursSlept = ""
                            minutesSlept = ""
                            saveTextData(logViewModel, hoursSlept, minutesSlept)
                        }
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            saveTextData(logViewModel, hoursSlept, minutesSlept)
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
                    value = minutesSlept,
                    onValueChange = {
                        minutesSlept = it
                        try {
                            saveTextData(logViewModel, hoursSlept, minutesSlept)
                        } catch (e: NumberFormatException) {
                            hoursSlept = ""
                            minutesSlept = ""
                            saveTextData(logViewModel, hoursSlept, minutesSlept)
                        }
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            saveTextData(logViewModel, hoursSlept, minutesSlept)
                        },
                    ),
                )
            }
            Spacer(modifier = Modifier.size(6.dp))
            Button(
                onClick = {
                    logViewModel.resetText(LogPrompt.Sleep)
                    hoursSlept = ""
                    minutesSlept = ""
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = appViewModel.colorPalette
                    .secondary1),
                modifier = Modifier
                    .padding(start = 16.dp),
            ) {
                Text(
                    "Clear",
                )
            }
        }
    }
}

private fun saveTextData(logViewModel: LogViewModel, hoursSlept: String, minutesSlept: String) {
    val time = Time(
        if (hoursSlept != "") hoursSlept.toInt() else 0,
        if (minutesSlept != "") minutesSlept.toInt() else 0,
        0,
    )
    logViewModel.setText(LogPrompt.Sleep, time.toString())
}
