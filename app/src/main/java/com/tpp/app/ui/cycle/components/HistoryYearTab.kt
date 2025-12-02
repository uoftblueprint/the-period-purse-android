package com.theperiodpurse.app.ui.cycle.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun YearTab(year: Int, selected: Boolean = false, onClick: () -> Unit) {
    val red = Color(0xFFB31F20)
    val outlineColor = if (selected) red else Color.LightGray
    val contentColor = if (selected) Color.White else Color.LightGray
    val backgroundColor = if (selected) red else Color.White
    Button(
        onClick = onClick,
        border = BorderStroke(1.dp, outlineColor),
        shape = RoundedCornerShape(25),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
        ),
        modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
    ) {
        Text(text = year.toString())
    }
}

@Preview
@Composable
fun YearTabFalsePreview() {
    YearTab(year = 2023, selected = false) {}
}

@Preview
@Composable
fun YearTabTruePreview() {
    YearTab(year = 2023, selected = true) {}
}
