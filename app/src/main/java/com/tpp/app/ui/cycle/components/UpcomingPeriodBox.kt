package com.tpp.app.ui.cycle.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tpp.app.R
import com.tpp.app.ui.viewmodel.AppViewModel


@Composable
fun UpcomingPeriodBox(text: String, appViewModel: AppViewModel? = null) {
    val color = appViewModel?.colorPalette?.secondary2 ?: Color.White
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(2f)
            )
            Image(
                modifier = Modifier
                    .height(120.dp)
                    .padding(12.dp)
                    .weight(1f),
                painter = painterResource(R.drawable.dykpad),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun UpcomingPeriodBoxPreview(){
    UpcomingPeriodBox("Your period will likely come any day now!")
}
