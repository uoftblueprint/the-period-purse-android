package com.tpp.theperiodpurse.ui.setting

import android.Manifest
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.model.Symptom
import com.tpp.theperiodpurse.ui.component.SocialMedia
import com.tpp.theperiodpurse.ui.legal.TermsAndPrivacyFooter
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.theme.Teal
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

/**
 * Displays the layout for the settings screen.
 *
 * @param modifier The modifier for the layout.
 * @param outController The navigation controller.
 * @param onNotificationClicked The callback function for notification button click.
 * @param onBackUpClicked The callback function for backup button click.
 * @param onDeleteClicked The callback function for delete button click.
 * @param appViewModel The view model for the app.
 * @param context The Android context.
 */
@Composable
fun SettingScreenLayout(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    outController: NavHostController,
    onNotificationClicked: () -> Unit,
    onBackUpClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    appViewModel: AppViewModel = viewModel(),
    context: Context,
) {
    val symptoms = appViewModel.getTrackedSymptoms()
    var hasAlarmPermission by remember {
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
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasAlarmPermission = isGranted
//                   if (!isGranted) {
//                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
// //                           shouldShowRequestPermissionRationale(SCHEDULE_EXACT_ALARM)
//                       }
//                   }
        },
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        SideEffect {
            launcher.launch(SCHEDULE_EXACT_ALARM)
        }
    }
    val time = appViewModel.getReminderFreq() + " at " + appViewModel.getReminderTime()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(R.string.tracking_preferences),
            modifier = modifier.padding(top = 30.dp, start = 10.dp),
            color = appViewModel.colorPalette.text1,
            fontWeight = FontWeight.Bold,
            fontSize = 20.scaledSp(),
        )

        TrackingPreferencesRow(symptoms, appViewModel = appViewModel, context = context)
        Text(
            text = stringResource(R.string.notifications_heading),
            modifier = modifier.padding(top = 5.dp, start = 10.dp),
            color = appViewModel.colorPalette.text1,
            fontWeight = FontWeight.Bold,
            fontSize = 20.scaledSp(),
        )
        Row(modifier = modifier.padding(20.dp)) {
            Column(modifier = Modifier) {
                Text(
                    text = stringResource(
                        R.string.remind_me_to_log_symptoms,
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.scaledSp(),
                    color = appViewModel.colorPalette.text1
                )
                Spacer(modifier = modifier.padding(3.dp))
                Text(
                    text = time,
                    modifier = Modifier.padding(start = 5.dp),
                    color = appViewModel.colorPalette.text2,
                    fontSize = 15.scaledSp(),
                )
            }
            Switch(
                enabled = false,
                checked = appViewModel.getAllowReminders(),
                onCheckedChange = { appViewModel.toggleAllowReminders(context) },
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = appViewModel.colorPalette.text2,
                ),
            )
        }

        Divider(
            modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .background(color = appViewModel.colorPalette.lineColor))

        NavigateButton(
            stringResource(id = R.string.customize_notifications),
            appViewModel = appViewModel,
            onClicked = onNotificationClicked,
        )

        Divider(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .background(color = appViewModel.colorPalette.lineColor))

        Text(
            text = stringResource(R.string.account_settings_heading),
            modifier = Modifier.padding(start = 10.dp, top = 30.dp),
            color = appViewModel.colorPalette.text1,
            fontWeight = FontWeight.Bold,
            fontSize = 20.scaledSp(),
        )

        Row(modifier = modifier.padding(start = 18.dp, end = 18.dp)) {
            Column(modifier = modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = stringResource(
                        R.string.toggle_color,
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.scaledSp(),
                    color = appViewModel.colorPalette.text1
                )
            }
            Switch(
                checked = appViewModel.getColorMode(),
                onCheckedChange = {
                    appViewModel.toggleColorMode(context)
                                  },
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = appViewModel.colorPalette.text1,
                ),
            )
        }

        Divider(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .background(color = appViewModel.colorPalette.lineColor))

        NavigateButton(
            text = stringResource(R.string.back_up_account),
            appViewModel= appViewModel,
            onClicked = onBackUpClicked,
        )
        Divider(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .background(color = appViewModel.colorPalette.lineColor))
        NavigateButton(
            text = stringResource(id = R.string.delete_account),
            appViewModel = appViewModel,
            onClicked = onDeleteClicked,
        )
        Divider(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .background(color = appViewModel.colorPalette.lineColor))
        Spacer(modifier = Modifier.padding(20.dp))
        val uriHandler = LocalUriHandler.current
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            SocialMedia(uriHandler)
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = "© 2023 The Period Purse. All rights reserved.",
            textAlign = TextAlign.Center,
            color = appViewModel.colorPalette.text1,
            fontSize = 15.scaledSp(),
        )

       /*
       Terms & Conditions, and Privacy Policy
        */
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            TermsAndPrivacyFooter(outController, appViewModel.colorPalette.text1)
            Spacer(modifier = Modifier.size(80.dp))
        }
        Spacer(modifier = Modifier
            .size(80.dp)
            .padding(bottom = 5.dp))
    }
}

/**
 * Displays the row for tracking preferences.
 *
 * @param symptoms The list of tracked symptoms.
 * @param modifier The modifier for the row.
 * @param appViewModel The view model for the app.
 */
@Composable
fun TrackingPreferencesRow(symptoms: List<Symptom>, modifier: Modifier = Modifier, appViewModel: AppViewModel, context: Context) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.mood),
            icon = painterResource(id = R.drawable.sentiment_neutral_black_24dp),
            contentDescription = stringResource(R.string.mood),
            ischecked = symptoms.contains(Symptom.MOOD),
            symptom = Symptom.MOOD,
            appViewModel = appViewModel,
            context = context,
        )
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.exercise),
            icon = painterResource(id = R.drawable.self_improvement_black_24dp),
            contentDescription = stringResource(R.string.exercise),
            ischecked = symptoms.contains(Symptom.EXERCISE),
            symptom = Symptom.EXERCISE,
            appViewModel = appViewModel,
            context = context,
        )
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.cramps),
            icon = painterResource(id = R.drawable.sick_black_24dp),
            contentDescription = stringResource(R.string.cramps),
            ischecked = symptoms.contains(Symptom.CRAMPS),
            symptom = Symptom.CRAMPS,
            appViewModel = appViewModel,
            context = context,
        )
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.sleep),
            icon = painterResource(id = R.drawable.nightlight_black_24dp),
            contentDescription = stringResource(R.string.sleep),
            ischecked = symptoms.contains(Symptom.SLEEP),
            symptom = Symptom.SLEEP,
            appViewModel = appViewModel,
            context = context,
        )
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.ovulation),
            icon = painterResource(id = R.drawable.ovulation_egg_24dp),
            contentDescription = stringResource(R.string.ovulation),
            ischecked = symptoms.contains(Symptom.OVULATION),
            symptom = Symptom.OVULATION,
            appViewModel = appViewModel,
            context = context,
        )
    }
}

/**
 * Displays a tracking option button.
 *
 * @param modifier The modifier for the button.
 * @param label The label text for the button.
 * @param icon The icon for the button.
 * @param contentDescription The content description for the icon.
 * @param ischecked The checked state of the button.
 * @param symptom The associated symptom for the button.
 * @param appViewModel The view model for the app.
 */
@Composable
fun TrackingOptionButton(
    modifier: Modifier,
    label: String,
    icon: Painter,
    contentDescription: String,
    ischecked: Boolean,
    symptom: Symptom,
    appViewModel: AppViewModel,
    context: Context,
) {
    val configuration = LocalConfiguration.current
    val screenwidth = configuration.screenWidthDp
    val color = if (appViewModel.isSymptomChecked(symptom)) Teal else appViewModel.colorPalette.CalendarDayColor
    Column(
        modifier = modifier
            .padding((screenwidth * 0.02).dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconToggleButton(
            checked = ischecked,
            onCheckedChange = { appViewModel.updateSymptoms(symptom, context) },
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
        ) {
            Icon(
                painter = icon,
                tint = Color.DarkGray,
                contentDescription = contentDescription,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(color)
                    .height((screenwidth * 0.12).dp)
                    .width((screenwidth * 0.12).dp)
                    .padding((screenwidth * 0.02).dp),
            )
        }
        Text(
            modifier = Modifier.padding(5.dp),
            text = label,
            fontSize = 12.scaledSp(),
            color = appViewModel.colorPalette.text1
        )
    }
}

/**
 * Displays a navigate button.
 *
 * @param text The button text.
 * @param onClicked The callback function for button click.
 */
@Composable
fun NavigateButton(text: String, appViewModel: AppViewModel, onClicked: () -> Unit) {
    Button(
        onClick = onClicked,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            Color.Transparent,
        ),
        elevation = ButtonDefaults.elevation(0.dp),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.scaledSp(),
            color = appViewModel.colorPalette.text1
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "arrow",
            modifier = Modifier.wrapContentWidth(Alignment.End),
            tint = appViewModel.colorPalette.MainFontColor
        )
    }
}
