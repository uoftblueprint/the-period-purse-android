package com.tpp.theperiodpurse.ui.education

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.component.Background
import com.tpp.theperiodpurse.ui.component.SocialMedia
import com.tpp.theperiodpurse.ui.datasource.Product
import com.tpp.theperiodpurse.ui.datasource.ProductsList
import com.tpp.theperiodpurse.ui.legal.TermsAndPrivacyFooter
import com.tpp.theperiodpurse.ui.onboarding.scaledSp
import com.tpp.theperiodpurse.ui.theme.MainFontColor
import com.tpp.theperiodpurse.ui.theme.Teal
import com.tpp.theperiodpurse.utility.getFact
import com.tpp.theperiodpurse.utility.getSubString
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

const val gray = 0xFF6D6E71
const val teal = 0xFF72C6B7
const val pink = 0xFFFFA3A4

@Composable
fun EducationScreenLayout(
    appViewModel: AppViewModel,
    outController: NavHostController = rememberNavController(),
    navController: NavHostController,
) {
    val uriHandler = LocalUriHandler.current
    EducationScreenContent(navController, uriHandler, outController, appViewModel)
}

@Composable
fun EducationScreenContent(
    navController: NavHostController,
    uriHandler: UriHandler,
    outController: NavHostController,
    appViewModel: AppViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .safeDrawingPadding()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            state = rememberLazyGridState(),
        ) {
            item(span = { GridItemSpan(2) }) {
                TopSection(navController)
            }
            items(ProductsList, span = { GridItemSpan(1) }) {
                PeriodProducts(navController, it, appViewModel)
            }
            item(span = { GridItemSpan(2) }) {
                BottomSection(uriHandler, outController, appViewModel = appViewModel)
            }
        }
    }
}

@Composable
fun TopSection(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
    ) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            DYKCard(navController)
        }
        Text(
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Left,
            color = Color(gray),
            fontWeight = FontWeight.W800,
            fontSize = 15.scaledSp(),
            text = stringResource(R.string.tap_to_learn_more),
        )
    }
}

@Composable
fun BottomSection(uriHandler: UriHandler, outController: NavHostController, appViewModel: AppViewModel) {
    Column {
        TPPCard(uriHandler, appViewModel = appViewModel)
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            SocialMedia(uriHandler)
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.copyright),
            textAlign = TextAlign.Center,
            fontSize = 15.scaledSp(),
            color = appViewModel.colorPalette.MainFontColor,
        )
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            TermsAndPrivacyFooter(outController, appViewModel.colorPalette.MainFontColor)
        }
        Spacer(modifier = Modifier.size(64.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DYKCard(navController: NavHostController) {
    val context = LocalContext.current
    val currFact: String = getFact(context)!!
    Card(
        modifier = Modifier.padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 10.dp,
        backgroundColor = Teal,
        onClick = { navController.navigate(EducationNavigation.DYK.name) },
    ) {

        Row {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 12.dp, horizontal = 24.dp),
                    textAlign = TextAlign.Left,
                    color = Color.White,
                    fontWeight = FontWeight.W800,
                    text = stringResource(R.string.did_you_know),
                )
                Text(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 13.scaledSp(),
                    maxLines = 2,
                    text = getSubString(currFact, 40)
                )
            }
            Image(
                modifier = Modifier
                    .height(120.dp)
                    .padding(12.dp),
                painter = painterResource(R.drawable.dykpad),
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PeriodProducts(navController: NavHostController, it: Product, appViewModel: AppViewModel) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .height(IntrinsicSize.Min)
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        elevation = 10.dp,
        backgroundColor = appViewModel.colorPalette.productBackground,
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "elementId",
                value = it.ProductName,
            )
            navController.navigate(EducationNavigation.ProductInfo.name)
        },
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(2.dp),
                painter = painterResource(id = it.imageID),
                contentDescription = stringResource(
                    R.string.product_image_description,
                    it,
                ),
            )
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontWeight = Bold,
                fontSize = 18.scaledSp(),
                text = it.ProductName,
            )
        }
    }
}

@Composable
fun TPPCard(uriHandler: UriHandler, appViewModel: AppViewModel) {
    Card(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 28.dp)
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(12.dp),
        elevation = 10.dp,
        backgroundColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .background(color = appViewModel.colorPalette.HeaderColor1)
                .wrapContentSize(Alignment.Center),
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = Bold,
                text = stringResource(R.string.learn_more),
                fontSize = 15.scaledSp(),
                color = appViewModel.colorPalette.MainFontColor
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.strives_to_achieve_short),
                fontSize = 13.scaledSp(),
                color = appViewModel.colorPalette.MainFontColor
            )
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Teal,
                    contentColor = appViewModel.colorPalette.MainFontColor,
                ),
                onClick = { uriHandler.openUri("https://www.theperiodpurse.com/") },
            ) {
                Text(text = "Visit the website", fontSize = 15.scaledSp())
            }
        }
    }
}
