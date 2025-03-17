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
import androidx.compose.ui.zIndex
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
    val scrollState = rememberScrollState()

    EducationBackground(appViewModel = appViewModel)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fixed back button at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .zIndex(1f)
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) { navController.navigate(EducationNavigation.Learn.name) }
                    .size(20.dp),
                painter = painterResource(R.drawable.arrow),
                contentDescription = stringResource(R.string.back_button_label),
                tint = Teal,
            )
        }

        // Scrollable content with padding to avoid overlap with the fixed back button
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Product title
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 32.scaledSp(),
                text = "How to use",
                color = appViewModel.colorPalette.MainFontColor
            )

            // Track step number
            var stepNumber = 1

            // Iterate through description blocks
            product.descriptionBlocks.forEach { block ->
                when (block) {
                    is TextBlock -> {
                        if (block.text == "With Applicator" || block.text == "Without Applicator") {
                            // Handle special indicator style
                            ApplicatorIndicator(block.text)

                            // Reset step number to 1 if "Without Applicator" is encountered
                            if (block.text == "Without Applicator") {
                                stepNumber = 1
                            }
                        } else {
                            // Regular text block (part of a step)
                            Text(
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center,
                                text = block.text,
                                fontSize = 18.scaledSp(),
                                color = appViewModel.colorPalette.MainFontColor
                            )
                        }
                    }
                    is ImageBlock -> {
                        // Start a new step for each image block
                        StepIndicator(stepNumber)
                        stepNumber++

                        // Display the image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = block.imageResId),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun ApplicatorIndicator(label: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Teal)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.height(8.dp)) // Space below the indicator
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