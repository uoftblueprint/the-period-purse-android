package com.theperiodpurse.app.ui.symptomlog

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import com.theperiodpurse.app.data.model.LogPrompt
import com.theperiodpurse.app.ui.theme.LogSelectedTextColor
import com.theperiodpurse.app.ui.theme.MainFontColor
import com.theperiodpurse.app.ui.viewmodel.AppViewModel
import com.theperiodpurse.app.ui.viewmodel.LogViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotesPrompt(logViewModel: LogViewModel, appViewModel: AppViewModel) {
    var notesText by remember { mutableStateOf(logViewModel.getText(LogPrompt.Notes)) }
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
                saveTextData(logViewModel, notesText)
                focusManager.clearFocus(true)
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        logViewModel.setText(LogPrompt.Notes, notesText)
                        focusManager.clearFocus()
                    })
                },

        ) {
            MinLinesOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = notesText,
                onValueChange = {
                        notes: String ->
                    notesText = notes
                    saveTextData(logViewModel, notesText)
                },
                placeholder = { Text(text = "Record a symptom or make a note", color =
                appViewModel.colorPalette.MainFontColor) },
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        saveTextData(logViewModel, notesText)
                        focusManager.clearFocus()
                    },
                ),
                minLines = 3,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = appViewModel.colorPalette.MainFontColor,
                    unfocusedBorderColor = LogSelectedTextColor,
                    focusedBorderColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    cursorColor = appViewModel.colorPalette.primary1,
                    backgroundColor = appViewModel.colorPalette.secondary4
                ),
            )
        }
    }
}

private fun saveTextData(logViewModel: LogViewModel, notesText: String) {
    logViewModel.setText(LogPrompt.Notes, notesText)
    Log.d("Type", notesText)
}

@Composable
fun MinLinesOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    minLines: Int,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
) {
    val heightState = remember { mutableStateOf<Int?>(null) }
    var heightUpdateNeeded by remember(modifier, textStyle) { mutableStateOf(true) }
    val height = with(LocalDensity.current) {
        heightState.value?.toDp()
    } // to use if nullable unwrapping
    Box(
        modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Min),
    ) {
        if (heightUpdateNeeded) {
            OutlinedTextField(
                value = value + "\n".repeat(minLines),
                onValueChange = onValueChange,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                label = label,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                isError = isError,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                interactionSource = interactionSource,
                shape = shape,
                colors = colors,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0f)
                    .onSizeChanged {
                        heightUpdateNeeded = false
                        println("onSizeChanged $it")
                        heightState.value = it.height
                    },
            )
        }
        if (height != null) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                label = label,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                isError = isError,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                interactionSource = interactionSource,
                shape = shape,
                colors = colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
            )
        }
    }
}
