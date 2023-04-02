package com.tpp.theperiodpurse.ui.onboarding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavHostController
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.drive.Drive
import com.tpp.theperiodpurse.OnboardingScreen
import com.tpp.theperiodpurse.Screen
import com.tpp.theperiodpurse.ui.setting.SettingScreenNavigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoadGoogleDrive(googleDrive: Drive, viewModel: OnboardViewModel, navHostController: NavHostController) {

    val isDrive by viewModel.isDrive.observeAsState(initial = null)

    var confirmLoad = remember { mutableStateOf(false)  }

    var decision = remember { mutableStateOf(false)  }



    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            viewModel.checkGoogleDrive(googleDrive)
        }
    }
    if (isDrive == null){
        LoadingScreen()
    }
    else {

        if (!confirmLoad.value){
            AlertDialog(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Color.White,
                contentColor = Color.Black,
                onDismissRequest = { },
                title = {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Use Backup",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Text(
                        text = "Would you like to use your backed up data for this app?",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                },
                confirmButton = {
                    OutlinedButton(
                        onClick = {
                            confirmLoad.value = true
                            decision.value = true

                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .height(48.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text(
                            text = "Yes",
                            style = MaterialTheme.typography.button,
                            color = Color.Blue
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { confirmLoad.value = true },
                        modifier = Modifier
                            .padding(2.dp)
                            .height(48.dp)
                            .fillMaxWidth(),

                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text(
                            text = "No",
                            style = MaterialTheme.typography.button,
                            color = Color.Blue
                        )
                    }
                }
            )

        }

        if (isDrive!!.files.isNotEmpty() && confirmLoad.value && decision.value){
            LaunchedEffect(Unit) {
                navHostController.popBackStack()
                navHostController.navigate(Screen.Calendar.name)
            }
        }
        else if (confirmLoad.value) {
            LaunchedEffect(Unit) {
                navHostController.popBackStack()
                navHostController.navigate(OnboardingScreen.QuestionOne.name)
            }
        }


    }






}