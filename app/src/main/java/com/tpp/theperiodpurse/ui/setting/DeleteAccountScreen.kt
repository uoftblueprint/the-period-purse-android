package com.tpp.theperiodpurse.ui.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

/**
 * A composable function for the delete account screen.
 *
 * @param navController The NavHostController instance for navigation.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteAccountScreen(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    var confirmDelete = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = (screenHeight * 0.10).dp,
                start = (screenHeight * 0.03).dp,
                end = (screenHeight * 0.03).dp,
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column() {
            // Display the confirmation message
            Text(
                text = "Are you sure you want to delete your account? You cannot undo this action.",
                fontSize = 15.scaledSp(),
                color = appViewModel.colorPalette.MainFontColor
            )

            // Display the "Delete Account" button
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    confirmDelete.value = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(195, 50, 50)),
            ) {
                Text(text = "Delete Account", color = Color.White)
            }

            if (confirmDelete.value) {
                // Display the confirmation dialog
                AlertDialog(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = appViewModel.colorPalette.HeaderColor1,
                    contentColor = appViewModel.colorPalette.MainFontColor,
                    onDismissRequest = { confirmDelete.value = false },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Filled.Warning,
                                contentDescription = "Warning Icon",
                                tint = Color(195, 50, 50),
                                modifier = Modifier.size(24.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Delete Account",
                                style = MaterialTheme.typography.h6,
                            )
                        }
                    },
                    text = {
                        Text(
                            text = "Are you sure you want to delete your account?",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )
                    },
                    confirmButton = {
                        // Display the "Delete" button
                        OutlinedButton(
                            onClick = {
                                confirmDelete.value = false
                                navController.navigate(SettingScreenNavigation.ResetDatabase.name)
                            },
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(1f)
                                .height(48.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = appViewModel.colorPalette.HeaderColor1),
                        ) {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.button,
                                color = Color(195, 50, 50),
                            )
                        }
                    },
                    dismissButton = {
                        // Display the "Cancel" button
                        OutlinedButton(
                            onClick = { confirmDelete.value = false },
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(1f)
                                .height(48.dp)
                                .fillMaxWidth(),

                            colors = ButtonDefaults.buttonColors(backgroundColor = appViewModel.colorPalette.HeaderColor1),
                        ) {
                            Text(
                                text = "Cancel",
                                style = MaterialTheme.typography.button,
                                color = Color.Blue,
                            )
                        }
                    },
                )
            }
        }
    }
}
