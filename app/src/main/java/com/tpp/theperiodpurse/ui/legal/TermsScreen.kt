package com.tpp.theperiodpurse.ui.legal

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.component.TopNavBar
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TermsScreen(navController: NavHostController, appViewModel: AppViewModel) {
    val interactionSource = remember { MutableInteractionSource() }
    val screenName = stringResource(R.string.terms_and_conditions)

    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = { TopNavBar(screenName, navController, interactionSource, appViewModel) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Title(str = screenName, appViewModel)

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Gray,
                    thickness = 2.dp,
                )

                Body(stringResource(R.string.terms_and_conditions), appViewModel)

                Header(stringResource(R.string.intellectual_property), appViewModel)
                Body(stringResource(R.string.intellectual_property_details), appViewModel)

                Header(stringResource(R.string.accounts), appViewModel)
                Body(stringResource(R.string.account_details), appViewModel)

                Header(stringResource(R.string.limitation_of_liability), appViewModel)
                Body(stringResource(R.string.limitation_of_liability_details), appViewModel)

                Header(stringResource(R.string.indemnity), appViewModel)
                Body(stringResource(R.string.indemnity_details), appViewModel)

                Header(stringResource(R.string.applicable_law), appViewModel)
                Body(stringResource(R.string.applicable_law_details), appViewModel)

                Header(stringResource(R.string.severability), appViewModel)
                Body(stringResource(R.string.severability_details), appViewModel)

                Header(stringResource(R.string.terms_and_conditions), appViewModel)
                Body(stringResource(R.string.terms_conditions_changes_details), appViewModel)

                Header(stringResource(R.string.contact_details_string), appViewModel)
                Body(stringResource(R.string.contact_details), appViewModel)
                Body(stringResource(R.string.terms_conditions_effective_date), appViewModel)
                Spacer(modifier = Modifier.size(36.dp))
            }
        },
    )
}

