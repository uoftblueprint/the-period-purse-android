package com.tpp.theperiodpurse.ui.setting


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tpp.theperiodpurse.AppViewModel
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.Alarm
import com.tpp.theperiodpurse.data.MonthlyAlarm
import com.tpp.theperiodpurse.data.WeeklyAlarm
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class NotificationsScreen(private val appViewModel: AppViewModel) : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationsLayout(
                temp(), appViewModel
            )
        }
    }
}

fun temp(){

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NotificationsLayout(appBar: Unit, appViewModel: AppViewModel){

    val formatter = DateTimeFormatter.ofPattern("h:mm a") // define the format of the input string
    var formattedTime = appViewModel.getReminderTime()
    var pickedTime = LocalTime.parse(formattedTime, formatter)

    appBar

    val timeDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(text = stringResource(id = R.string.remind_me_to_log_symptoms),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(15.dp, top = 60.dp))
        Text(text = stringResource(id = R.string.reminder_description),
            modifier = Modifier.padding(start = 15.dp),
            fontWeight = FontWeight.Light)
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Expandable(appViewModel)
        Divider(color = Color.Gray, thickness = 0.7.dp)
        TimePicker(timeDialogState, formattedTime)
        Divider(color = Color.Gray, thickness = 0.7.dp)

    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                formattedTime = pickedTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                appViewModel.setReminderTime(formattedTime)
            }
            negativeButton(text = "Cancel")
        }
    ) {
        timepicker(
            initialTime = pickedTime,
            title = "Pick a time",
        ) {
            pickedTime = it
        }
    }

}

@RequiresApi(Build.VERSION_CODES.M)
fun cancelAlarm(context: Context, appViewModel: AppViewModel){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val freq = appViewModel.getReminderFreq()
    lateinit var intent: Intent
    if (freq == "Every day"){
        intent = Intent(context, Alarm::class.java)
    } else if(freq == "Every week"){
        intent = Intent(context, WeeklyAlarm::class.java)
    } else if (freq == "Every month"){
        intent = Intent(context, MonthlyAlarm::class.java)
    }

    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    alarmManager.cancel(pendingIntent)
}

@RequiresApi(Build.VERSION_CODES.S)
fun setAlarm(context: Context, pickedTime: LocalTime, appViewModel: AppViewModel, isFirstAlarm: Boolean){


    val curr_time = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }
    calendar.apply {
        set(Calendar.HOUR_OF_DAY,pickedTime.hour)
        set(Calendar.MINUTE,pickedTime.minute)
        set(Calendar.SECOND, pickedTime.second)
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    val intent = Intent(context, Alarm::class.java)

    val freq = appViewModel.getReminderFreq()
    lateinit var intent: Intent
    when (freq) {
        "Every day" -> {
            intent = Intent(context, Alarm::class.java)
        }
        "Every week" -> {
            intent = Intent(context, WeeklyAlarm::class.java)
        }
        "Every month" -> {
            intent = Intent(context, MonthlyAlarm::class.java)
        }
    }

    intent.putExtra("hasRemindersPermissions", appViewModel.getAllowReminders())
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

    val hasAlarmPermission: Boolean = alarmManager.canScheduleExactAlarms()


    if(hasAlarmPermission){
        if(isFirstAlarm && curr_time.timeInMillis > calendar.timeInMillis){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis + 86400000, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

}


@Composable
private fun TimePicker(
    timeDialogState: MaterialDialogState,
    formattedTime: String
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
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(id = R.string.reminder_time), modifier = Modifier.padding(start = 5.dp))
                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                        .background(Color.Transparent),
                    onClick = {
                        timeDialogState.show()
                    }) {
                    Text(
                        text = formattedTime,
                        style = TextStyle(color = Color(0xFF74C5B7)),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun Expandable(appViewModel: AppViewModel){
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
                .fillMaxWidth()
        ) {
            val radioOptions = listOf("Every day", "Every week", "Every month")
            val selectedOption = appViewModel.getReminderFreq()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(id = R.string.repeat), modifier = Modifier.padding(start = 5.dp))
                if (repeatExpanded) {
                    RadioButtons(radioOptions, selectedOption, appViewModel)
                }

            }
            Text(text = selectedOption, modifier =
            if(!repeatExpanded) Modifier.align(Alignment.CenterVertically) else Modifier.align(
                Alignment.Top
            ))
            IconButton(onClick = { repeatExpanded = !repeatExpanded }) {
                Icon(
                    imageVector = if (repeatExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (repeatExpanded) "Show less text" else "Show more text"
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
                                //onOptionSelected(text)
                                appViewModel.setReminderFreq(text)
                            }
                        )
                        .padding(horizontal = 15.dp)
                ) {
                    RadioButton(
                        selected = (text == selectedOption), modifier = Modifier.padding(all = Dp(value = 8F)),
                        onClick = {
                            appViewModel.setReminderFreq(text)
                        }
                    )
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}






