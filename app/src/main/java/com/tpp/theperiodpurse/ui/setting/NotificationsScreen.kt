package com.tpp.theperiodpurse.ui.setting

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.utility.NotificationHelper
import com.tpp.theperiodpurse.utility.alarm.Alarm
import com.tpp.theperiodpurse.utility.alarm.MonthlyAlarm
import com.tpp.theperiodpurse.utility.alarm.WeeklyAlarm
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Represents the notifications screen of the application.
 *
 * @property appViewModel The view model for the app.
 */
class NotificationsScreen(private val appViewModel: AppViewModel) : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            com.tpp.theperiodpurse.utility.NotificationHelper.createNotificationChannel(context)

            // Check if the app has notification permission
            val hasNotificationPermission by remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS,
                        ) == PackageManager.PERMISSION_GRANTED,
                    )
                } else {
                    mutableStateOf(true)
                }
            }

            NotificationsLayout(
                LocalContext.current,
                hasNotificationPermission,
                temp(),
                appViewModel,
            )
        }
    }
}

fun temp() {
    // Placeholder function
}



/**
 * Composable function for displaying the notifications layout.
 *
 * @param context The Android application context.
 * @param hasNotificationsPermission Indicates whether the app has notifications permission.
 * @param appBar The app bar component.
 * @param appViewModel The view model for the app.
 */
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NotificationsLayout(context: Context, hasNotificationsPermission: Boolean, appBar: Unit, appViewModel: AppViewModel) {
    val formatter = DateTimeFormatter.ofPattern("h:mm a") // define the format of the input string
    var formattedTime = appViewModel.getReminderTime()
    var pickedTime = LocalTime.parse(formattedTime, formatter)

    var ovulationNotificationsEnabled by remember {
        mutableStateOf(appViewModel.getOvulationNotificationEnabled())
    }

    appBar

    val timeDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(id = R.string.remind_me_to_log_symptoms),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(15.dp, top = 60.dp),
            fontSize = 20.scaledSp(),
            color = appViewModel.colorPalette.MainFontColor
        )
        Text(
            text = stringResource(id = R.string.reminder_description),
            modifier = Modifier.padding(start = 15.dp),
            fontWeight = FontWeight.Light,
            fontSize = 20.scaledSp(),
            color = appViewModel.colorPalette.MainFontColor
        )
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Expandable(appViewModel)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enable Ovulation Notifications",
                modifier = Modifier.weight(1f),
                fontSize = 18.scaledSp(),
                fontWeight = FontWeight.Bold,
                color = appViewModel.colorPalette.MainFontColor
            )
            Switch(
                checked = ovulationNotificationsEnabled,
                onCheckedChange = { isChecked ->
                    ovulationNotificationsEnabled = isChecked
                    appViewModel.setOvulationNotificationEnabled(isChecked)
                    if (isChecked) {
                        val periodHistory = ArrayList(appViewModel.getDates())
                        NotificationHelper.sendOvulationNotification(context, periodHistory)
                    }
                }
            )
        }


        Divider(color = Color.Gray, thickness = 0.7.dp)


        TimePicker(timeDialogState, formattedTime)
    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                formattedTime = pickedTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                appViewModel.setReminderTime(formattedTime, context)
                if (hasNotificationsPermission) {
                    if (appViewModel.getAllowReminders()) {
                        setAlarm(context, pickedTime, appViewModel)
                        println("alarm set")
                    } else {
                        println("didn't work")
                    }
                } else { println("alarm not set") }
            }
            negativeButton(text = "Cancel")
        },
    ) {
        timepicker(
            initialTime = pickedTime,
            title = "Pick a time",
        ) {
            pickedTime = it
        }
    }
}

/**
 * Sets the alarm for the specified time.
 *
 * @param context The Android application context.
 * @param pickedTime The time at which the alarm should be set.
 * @param appViewModel The view model for the app.
 */
@RequiresApi(Build.VERSION_CODES.S)
fun setAlarm(context: Context, pickedTime: LocalTime, appViewModel: AppViewModel) {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }
    calendar.apply {
        set(Calendar.HOUR_OF_DAY, pickedTime.hour)
        set(Calendar.MINUTE, pickedTime.minute)
        set(Calendar.SECOND, pickedTime.second)
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val freq = appViewModel.getReminderFreq()
    lateinit var intent: Intent
    if (freq == "Every day") {
        intent = Intent(context, Alarm::class.java)
    } else if (freq == "Every week") {
        intent = Intent(context, WeeklyAlarm::class.java)
    } else if (freq == "Every month") {
        intent = Intent(context, MonthlyAlarm::class.java)
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val hasAlarmPermission: Boolean = alarmManager.canScheduleExactAlarms()

    if (hasAlarmPermission) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}

@Composable
private fun TimePicker(
    timeDialogState: MaterialDialogState,
    formattedTime: String,
) {
    val repeatExpanded by remember {
        mutableStateOf(false)
    }

    val extraPadding by animateDpAsState(
        targetValue = if (repeatExpanded) 48.dp else 0.dp,
    )

    Column {
        Row(
            modifier = Modifier
                //        .padding(24.dp)
                .background(Color.White.copy(alpha = 0.8f))
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                    .align(Alignment.CenterVertically),
            ) {
                Text(
                    text = stringResource(id = R.string.reminder_time),
                    modifier = Modifier.padding(start = 5.dp),
                    fontSize = 18.scaledSp(),
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                elevation = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .background(Color.Transparent),
                onClick = {
                    timeDialogState.show()
                },
            ) {
                Text(
                    text = formattedTime,
                    style = TextStyle(color = Color(0xFF74C5B7)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.scaledSp(),
                )
            }
        }
    }
}

@Composable
fun Expandable(appViewModel: AppViewModel) {
    var repeatExpanded by remember {
        mutableStateOf(false)
    }

    val extraPadding by animateDpAsState(
        targetValue = if (repeatExpanded) 48.dp else 0.dp,
    )

    Column {
        Row(
            modifier = Modifier
                //        .padding(24.dp)
                .background(Color.White)
                .fillMaxWidth(),
        ) {
            val radioOptions = listOf("Every day", "Every week", "Every month")
            val selectedOption = appViewModel.getReminderFreq()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                    .align(Alignment.CenterVertically),
            ) {
                Text(
                    text = stringResource(id = R.string.repeat),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 5.dp),
                    fontSize = 18.scaledSp(),
                )
                if (repeatExpanded) {
                    RadioButtons(radioOptions, selectedOption, appViewModel)
                }
            }
            Text(
                text = selectedOption,
                style = TextStyle(color = Color(0xFF74C5B7)),
                fontSize = 18.scaledSp(),
                modifier =
                if (!repeatExpanded) {
                    Modifier.align(Alignment.CenterVertically)
                } else Modifier.align(
                    Alignment.Top,
                ),
            )
            IconButton(onClick = { repeatExpanded = !repeatExpanded }) {
                Icon(
                    imageVector = if (repeatExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (repeatExpanded) "Show less text" else "Show more text",
                )
            }
        }
    }
}

@Composable
fun RadioButtons(radioOptions: List<String>, selectedOption: String, appViewModel: AppViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                // onOptionSelected(text)
                                appViewModel.setReminderFreq(text)
                            },
                        )
                        .padding(horizontal = 15.dp),
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        modifier = Modifier.padding(all = Dp(value = 8F)),
                        onClick = {
                            appViewModel.setReminderFreq(text)
                        },
                    )
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.CenterVertically),
                        fontSize = 18.scaledSp(),
                    )
                }
            }
        }
    }
}
