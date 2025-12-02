package com.theperiodpurse.app.ui.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * A composable function for displaying a confirmation dialog after a backup operation.
 *
 * @param navController The NavHostController instance for navigation.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmBackUp(navController: NavHostController) {
    var confirmBackUp = remember { mutableStateOf(false) }

    // Display the confirmation dialog
    AlertDialog(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White,
        contentColor = Color.Black,
        onDismissRequest = {
            confirmBackUp.value = false
            navController.navigate(SettingScreenNavigation.Start.name)
        },
        title = {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Backup Successful",
                    style = MaterialTheme.typography.h6,
                )
            }
        },
        text = {
            Text(
                text = "Your data has been backed up to your Google Drive!",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = {
            // Display the "OK" button
            OutlinedButton(
                onClick = {
                    confirmBackUp.value = false
                    navController.navigate(SettingScreenNavigation.Start.name)
                },
                modifier = Modifier
                    .padding(2.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            ) {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.button,
                    color = Color.Blue,
                )
            }
        },
    )
}
