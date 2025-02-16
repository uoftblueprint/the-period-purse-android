package com.tpp.theperiodpurse.ui.education

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.datasource.*
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.theme.Teal
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

@Composable
fun EducationInfoScreen(
    appViewModel: AppViewModel,
    navController: NavHostController,
    elementId: String,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val product = ProductsList.find { it.productName == elementId } ?: Product()

    EducationBackground(appViewModel = appViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Back button
        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { navController.navigate(EducationNavigation.Learn.name) }
                .size(20.dp)
                .align(Alignment.Start),
            painter = painterResource(R.drawable.arrow),
            contentDescription = stringResource(R.string.back_button_label),
            tint = Teal,
        )

        // Product title
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 32.scaledSp(),
            text = "How to use",
            color = appViewModel.colorPalette.MainFontColor
        )

        // Group content into steps
        var stepNumber = 1
        val groupedBlocks = product.descriptionBlocks.chunked(2) // Groups items in pairs

        groupedBlocks.forEach { group ->
            // Step number indicator at the top of each group
            StepIndicator(stepNumber)
            stepNumber++

            group.forEach { block ->
                when (block) {
                    is TextBlock -> {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center,
                            text = block.text,
                            fontSize = 18.scaledSp(),
                            color = appViewModel.colorPalette.MainFontColor
                        )
                    }
                    is ImageBlock -> {
                        // Wrap the Image in a Box to center it horizontally
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = block.imageResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth(0.8f) // Adjust width if needed
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun StepIndicator(stepNumber: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Teal),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stepNumber.toString(),
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.height(8.dp)) // Space below the step indicator
}