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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.data.model.Symptom
import com.tpp.theperiodpurse.ui.education.SocialMedia
import com.tpp.theperiodpurse.ui.education.teal
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.ui.legal.TermsAndPrivacyFooter
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel


@Composable
fun SettingScreenLayout(
    modifier: Modifier = Modifier,
    outController: NavHostController,
    onNotificationClicked: () -> Unit,
    onBackUpClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    appViewModel: AppViewModel = viewModel(),
    context: Context
){
    val symptoms = appViewModel.getTrackedSymptoms()
    var hasAlarmPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasAlarmPermission = isGranted
//                   if (!isGranted) {
//                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
////                           shouldShowRequestPermissionRationale(SCHEDULE_EXACT_ALARM)
//                       }
//                   }
        }
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        SideEffect {
            launcher.launch(SCHEDULE_EXACT_ALARM)
        }
    }
    val time = appViewModel.getReminderFreq() + " at " + appViewModel.getReminderTime()

   Column(modifier = modifier
       .fillMaxSize()
       .padding(10.dp)
       .verticalScroll(rememberScrollState())) {
       Text(
           text = stringResource(R.string.tracking_preferences),
           modifier = modifier.padding(top = 30.dp, start = 10.dp),
           color = Color.DarkGray,
           fontWeight = FontWeight.Bold,
           fontSize = 20.scaledSp()
       )

       TrackingPreferencesRow(symptoms, appViewModel = appViewModel, context = context)
       Text(
           text = stringResource(R.string.notifications_heading),
           modifier = modifier.padding(top = 5.dp, start = 10.dp),
           color = Color.DarkGray,
           fontWeight = FontWeight.Bold,
           fontSize = 20.scaledSp()
       )
       Row(modifier = modifier.padding(20.dp)) {
           Column (modifier = Modifier) {
               Text(text = stringResource(
                   R.string.remind_me_to_log_symptoms),
                   fontWeight = FontWeight.Bold,
                   fontSize = 15.scaledSp(),)
               Spacer(modifier = modifier.padding(3.dp))
               Text(text = time,
                   modifier = Modifier.padding(start = 5.dp),
                   color = Color.Gray,
                   fontSize = 15.scaledSp(),
               )
           }
           Switch(
               enabled = false,
               checked = appViewModel.getAllowReminders(),
               onCheckedChange = {appViewModel.toggleAllowReminders(context)},
               modifier = modifier
                   .fillMaxWidth()
                   .wrapContentWidth(Alignment.End),
               colors = SwitchDefaults.colors(
                   uncheckedThumbColor = Color.DarkGray
               )
           )
       }
       Divider(modifier = Modifier.padding(start= 10.dp, end = 10.dp))

       NavigateButton(stringResource(id = R.string.customize_notifications),
           onClicked = onNotificationClicked
           )

       Divider(modifier = Modifier.padding(start= 10.dp, end = 10.dp))

       Text(
           text = stringResource(R.string.account_settings_heading),
           modifier = Modifier.padding(start= 10.dp, top = 30.dp),
           color = Color.DarkGray,
           fontWeight = FontWeight.Bold,
           fontSize = 20.scaledSp()
       )
       NavigateButton( text = stringResource(R.string.back_up_account),
           onClicked = onBackUpClicked
       )
       Divider(modifier = Modifier.padding(start= 10.dp, end = 10.dp))
       NavigateButton(text = stringResource(id = R.string.delete_account),
           onClicked = onDeleteClicked
       )
       Divider(modifier = Modifier.padding(start= 10.dp, end = 10.dp))
       Spacer(modifier = Modifier.padding(20.dp))
       val uriHandler = LocalUriHandler.current
       Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
           SocialMedia(uriHandler)
       }

       Text(modifier = Modifier
           .padding(horizontal = 8.dp, vertical = 12.dp)
           .align(Alignment.CenterHorizontally),
           text = "© 2023 The Period Purse. All rights reserved.",
           textAlign = TextAlign.Center,
           color = Color.DarkGray,
           fontSize = 15.scaledSp()
       )

       /*
       Terms & Conditions, and Privacy Policy
        */
       Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
           TermsAndPrivacyFooter(outController)
           Spacer(modifier = Modifier.size(80.dp))
       }
       Spacer(modifier = Modifier.size(80.dp).padding(bottom = 5.dp))
   }
}

@Composable
fun TrackingPreferencesRow(symptoms: List<Symptom>, modifier: Modifier = Modifier, appViewModel: AppViewModel, context: Context){

    Row(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.mood),
            icon = painterResource(id = R.drawable.sentiment_neutral_black_24dp),
            contentDescription = stringResource(R.string.mood),
            ischecked = symptoms.contains(Symptom.MOOD),
            symptom = Symptom.MOOD,
            appViewModel = appViewModel,
            context = context
        )
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.exercise),
            icon = painterResource(id = R.drawable.self_improvement_black_24dp),
            contentDescription = stringResource(R.string.exercise),
            ischecked = symptoms.contains(Symptom.EXERCISE),
            symptom = Symptom.EXERCISE,
            appViewModel = appViewModel,
            context = context
        )
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.cramps),
            icon = painterResource(id = R.drawable.sick_black_24dp),
            contentDescription = stringResource(R.string.cramps),
            ischecked = symptoms.contains(Symptom.CRAMPS),
            symptom = Symptom.CRAMPS,
            appViewModel = appViewModel,
            context = context
        )
        TrackingOptionButton(
            modifier = modifier,
            label = stringResource(R.string.sleep),
            icon = painterResource(id = R.drawable.nightlight_black_24dp),
            contentDescription = stringResource(R.string.sleep),
            ischecked = symptoms.contains(Symptom.SLEEP),
            symptom = Symptom.SLEEP,
            appViewModel = appViewModel,
            context = context
        )
    }
}

@Composable
fun TrackingOptionButton(modifier: Modifier, label: String, icon: Painter,
                         contentDescription: String, ischecked: Boolean,
                         symptom: Symptom, appViewModel: AppViewModel, context: Context
) {

    val configuration = LocalConfiguration.current
    val screenwidth = configuration.screenWidthDp;
    val color = if (appViewModel.isSymptomChecked(symptom)) Color(teal) else Color.White
    Column(
        modifier = modifier
            .padding((screenwidth *0.02).dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,) {
        IconToggleButton(
            checked = ischecked,
            onCheckedChange = {appViewModel.updateSymptoms(symptom, context)},
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(color)
                    .height((screenwidth *0.12).dp)
                    .width((screenwidth *0.12).dp)
                    .padding((screenwidth *0.02).dp)
            )

        }
        Text(modifier = Modifier.padding(5.dp),
            text = label,
            fontSize = 12.scaledSp())
    }
}

@Composable
fun NavigateButton(text: String, onClicked: () -> Unit ){
    Button(onClick = onClicked, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
        Color.Transparent), elevation = ButtonDefaults.elevation(0.dp)) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.scaledSp()
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "arrow",
            modifier = Modifier.wrapContentWidth(Alignment.End)
        )

    }
}
