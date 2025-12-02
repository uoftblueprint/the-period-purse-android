package com.tpp.app.ui.cycle.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tpp.app.R
import com.tpp.app.ui.onboarding.scaledSp
import com.tpp.app.ui.theme.DarkColorPaletteImpl

@Composable
fun AverageLengthBox(
    modifier: Modifier = Modifier,
    title: String,
    image: Painter,
    length: Float,
    color: Color,
) {
    Card(
        modifier = modifier.height(110.dp),
        elevation = 2.dp,
        backgroundColor = color,
        shape = RoundedCornerShape(10),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            Text(
                text = title,
                fontSize = 13.scaledSp(),
                fontWeight = FontWeight(700),
                color = Color.Black,
            )
            AverageLengthRow(
                length = length,
                image = image,
            )
        }
    }
}

@Composable
private fun AverageLengthRow(
    length: Float,
    modifier: Modifier = Modifier,
    image: Painter,
) {
    Row (modifier = modifier.padding(top = 10.dp)){
        Text(
            text = when (length) {
                (-1).toFloat() -> stringResource(R.string.log_to_learn)
                (-2).toFloat() -> stringResource(R.string.log_to_learn)
                else -> "${length.toInt()} Days"
            },
            fontSize = when (length) {
                (-1).toFloat() -> 13.scaledSp()
                (-2).toFloat() -> 13.scaledSp()
                else -> 20.scaledSp()
            },
            fontWeight = FontWeight(500),
            modifier = modifier
                .weight(1f),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier
                .clip(RoundedCornerShape(50))
                .background(color = Color.White)
                .aspectRatio(1f) // Maintain a 1:1 aspect ratio
                .weight(0.5f)
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .padding(10.dp), // Add padding to shrink the image inside the box
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
            )
        }
    }
}

@Preview
@Composable
fun AverageLengthBoxPreview(){
    AverageLengthBox(title = "Average Period Length", image = painterResource(R.drawable
        .flow_with_heart), length = 5.00f, color = DarkColorPaletteImpl().secondary3,)
}