package com.tpp.theperiodpurse.ui.education

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.datasource.Product
import com.tpp.theperiodpurse.ui.datasource.ProductsList
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
    var product = Product()
    ProductsList.forEach {
        if (it.ProductName == elementId) {
            product = it
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier
                .height(200.dp)
                .align(Alignment.CenterHorizontally)
                .padding(12.dp),
            painter = painterResource(id = product.imageID),
            contentDescription = stringResource(
                R.string.product_image_description,
                elementId,
            ),
        )
        Text(
            fontWeight = Bold,
            fontSize = 32.scaledSp(),
            text = product.ProductName,
            color = appViewModel.colorPalette.MainFontColor
        )
        Text(
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center,
            text = product.description,
            fontSize = 18.scaledSp(),
            color = appViewModel.colorPalette.MainFontColor
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}
