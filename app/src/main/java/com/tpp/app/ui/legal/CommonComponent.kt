package com.tpp.app.ui.legal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpp.app.ui.onboarding.scaledSp
import com.tpp.app.ui.viewmodel.AppViewModel

@Composable
fun Body(str: String, appViewModel: AppViewModel) {
    Text(
        fontSize = 14.scaledSp(),
        lineHeight = 16.scaledSp(),
        text = str,
        color = appViewModel.colorPalette.MainFontColor
    )
}

@Composable
fun Header(str: String, appViewModel: AppViewModel) {
    Column {
        Spacer(modifier = Modifier.size(12.dp))

        Text(
            modifier = Modifier.padding(vertical = 2.dp),
            fontSize = 14.scaledSp(),
            fontWeight = FontWeight.Bold,
            lineHeight = 24.scaledSp(),
            text = str,
            color = appViewModel.colorPalette.MainFontColor
        )
    }
}

@Composable
fun Title(str: String, appViewModel: AppViewModel) {
    Text(
        fontSize = 32.scaledSp(),
        fontWeight = FontWeight.Bold,
        text = str,
        color = appViewModel.colorPalette.MainFontColor
    )
}
