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
fun PrivacyScreen(navController: NavHostController, appViewModel: AppViewModel) {
    val interactionSource = remember { MutableInteractionSource() }
    val screenName = stringResource(R.string.privacy_policy)

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

                Header(stringResource(R.string.privacy_statement), appViewModel)
                Body(stringResource(R.string.strives_to_achieve), appViewModel)

                Header(stringResource(R.string.what_do_we_do_with_your_information), appViewModel)
                Body(stringResource(R.string.no_personal_info_access), appViewModel)

                Header(stringResource(R.string.did_you_get_my_consent), appViewModel)
                Body(stringResource(R.string.consent_response), appViewModel)

                Header(stringResource(R.string.did_you_collect_my_data), appViewModel)
                Body(stringResource(R.string.data_collection_response), appViewModel)

                Header(stringResource(R.string.do_you_share_my_information_with_anyone), appViewModel)
                Body(stringResource(R.string.information_sharing_response), appViewModel)

                Header(stringResource(R.string.is_my_information_protected), appViewModel)
                Body(stringResource(R.string.information_protected_response), appViewModel)

                Header(stringResource(R.string.i_am_underage_can_i_use_this_app), appViewModel)
                Body(stringResource(R.string.under_age_response), appViewModel)

                Header(stringResource(R.string.changes_to_this_privacy_policy), appViewModel)
                Body(stringResource(R.string.policy_update_response), appViewModel)

                Header(stringResource(R.string.questions_and_contact_information), appViewModel)
                Body(stringResource(R.string.contact), appViewModel)

                Spacer(modifier = Modifier.size(36.dp))
            }
        },
    )
}

