package com.tpp.theperiodpurse.ui.onboarding

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.OnboardingScreen
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.component.Background
import com.tpp.theperiodpurse.ui.state.OnboardUIState
import com.tpp.theperiodpurse.ui.theme.ButtonDisabledColor
import com.tpp.theperiodpurse.ui.theme.DarkColorPaletteImpl
import com.tpp.theperiodpurse.ui.theme.Teal
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel
import java.lang.Appendable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuestionOneScreen(
    modifier: Modifier = Modifier,
    onSelectionChanged: (String) -> Unit = {},
    canNavigateBack: Boolean,
    onboardUiState: OnboardUIState,
    navController: NavHostController,
    viewModel: OnboardViewModel,
    context: Context,
    signOut: () -> Unit = {},
) {
    var periodCycle by rememberSaveable { mutableStateOf("") }
    var entered by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val screenwidth = configuration.screenWidthDp
    val screenheight = configuration.screenHeightDp
    if (periodCycle == "") {
        entered = false
    }
    Background()
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .safeDrawingPadding()
    ) {
        Backbutton({
            onboardUiState.days = 0
            onboardUiState.symptomsOptions = listOf()
            onboardUiState.date = ""
            if (viewModel.checkGoogleLogin(context)) {
                signOut()
            }
            onboardUiState.googleAccount = null
            navController.navigate(OnboardingScreen.Welcome.name)
        }, canNavigateBack)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val ratio = 0.5
            val ratioimage = 0.2
            val height = (screenheight * ratio)
            val imageheight = (screenheight * ratioimage)
            Spacer(Modifier.height((screenheight * (0.02)).dp))
            Box(
                modifier = Modifier
                    .width(screenwidth.dp)
                    .height(height.dp),
            ) {
                Background_shape()
                Image(
                    painter = painterResource(R.drawable.last_period_length),
                    contentDescription = null,
                    modifier = Modifier
                        .size(imageheight.dp)
                        .align(Alignment.Center),
                )
                val barheight1 = (screenheight * (0.09))
                Image(
                    painter = painterResource(R.drawable.onboard_bar1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(barheight1.dp)
                        .align(Alignment.BottomCenter),
                )
            }
            Text(
                text = stringResource(R.string.question_one),
                fontSize = (screenheight * (0.035)).scaledSp(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height((screenheight * (0.01)).dp))
            Text(
                text = stringResource(R.string.description_one),
                fontSize = (screenheight * (0.03)).scaledSp(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width((screenwidth * (0.7)).dp),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height((screenheight * (0.02)).dp))
            EditDaysField(
                value = periodCycle,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    periodCycle = filteredValue
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        entered = periodCycle.isNotEmpty()
                        focusManager.clearFocus()
                    },
                ),
                entered = entered,
            )
            Spacer(Modifier.height((screenheight * (0.01)).dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = (screenheight * (0.02)).dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TextButton(
                    onClick = {
                        onboardUiState.days = 0
                        navController.navigate(OnboardingScreen.QuestionTwo.name)
                    },
                    modifier = modifier
                        .padding(start = (screenwidth * (0.1)).dp)
                        .weight(1f)
                        .semantics { contentDescription = "Skip" },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                    ),
                ) {
                    Text(
                        stringResource(R.string.skip),
                        color = Color.Black,
                        fontSize = 20.scaledSp(),
                    )
                }
                Button(
                    onClick = {
                        onSelectionChanged(periodCycle)
                        navController.navigate(OnboardingScreen.QuestionTwo.name)
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
}

@Composable
fun Backbutton(navigateUp: () -> Unit, canNavigateBack: Boolean) {
    TopAppBar(
        title = { "" },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Image(
                        painter = painterResource(R.drawable.back_icon),
                        contentDescription = "Back",
                    )
                }
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    )
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    value: String,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    appViewModel: AppViewModel,
    placeholder: String = "",
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(label), color = appViewModel.colorPalette.secondary1) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholder, color = appViewModel.colorPalette.secondary1) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            textColor = appViewModel.colorPalette.secondary1,
            backgroundColor = Color.Transparent,
            cursorColor = appViewModel.colorPalette.primary1,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = appViewModel.colorPalette.secondary1
        ),
    )
}

@Composable
fun EditDaysField(
    value: String,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    entered: Boolean,
) {
    TextField(
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                "Tap to input",
                fontSize = 17.scaledSp(),
                modifier = Modifier.padding(start = 10.dp),
            )
        },
        modifier = Modifier
            .width(175.dp)
            .height(60.dp)
            .background(color = Color.White, shape = RoundedCornerShape(20))
            .border(
                BorderStroke(2.dp, SolidColor(Color.Transparent)),
                shape = RoundedCornerShape(20),
            )
            .semantics { contentDescription = "Pick Days" },
        trailingIcon = {
            if (entered || value != "") {
                Text(
                    text = "days",
                    fontSize = 18.scaledSp(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 20.dp),
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.onboard_keyboard),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 0.dp, end = 20.dp),
                )
            }
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 20.scaledSp(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        ),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun Background_shape() {
    Image(
        painter = painterResource(R.drawable.background_shape__1_),
        contentDescription = null,
        modifier = Modifier.size(1000.dp),
    )
}
