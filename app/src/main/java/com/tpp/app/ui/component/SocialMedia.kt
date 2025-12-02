package com.tpp.app.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tpp.app.R
import com.tpp.app.ui.education.teal

@Composable
fun SocialMedia(uriHandler: UriHandler) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .clickable { uriHandler.openUri("https://www.instagram.com/theperiodpurse/") }
                .padding(horizontal = 8.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.instagram),
            contentDescription = stringResource(R.string.instagram),
            tint = Color(teal),
        )
        Icon(
            modifier = Modifier
                .clickable { uriHandler.openUri("https://www.tiktok.com/@theperiodpurse") }
                .padding(horizontal = 8.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.tiktok),
            contentDescription = stringResource(R.string.tiktok),
            tint = Color(teal),
        )
        Icon(
            modifier = Modifier
                .clickable {
                    uriHandler.openUri(
                        "https://www.youtube.com/channel/" + "UC2YgDU_9XxbjJsGGvXwxwyA",
                    )
                }
                .padding(horizontal = 8.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.youtube),
            contentDescription = stringResource(R.string.youtube),
            tint = Color(teal),
        )
        Icon(
            modifier = Modifier
                .clickable { uriHandler.openUri("https://twitter.com/ThePeriodPurse") }
                .padding(horizontal = 8.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.twitter),
            contentDescription = stringResource(R.string.twitter),
            tint = Color(teal),
        )
        Icon(
            modifier = Modifier
                .clickable { uriHandler.openUri("https://www.facebook.com/theperiodpurse") }
                .padding(horizontal = 8.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.facebook),
            contentDescription = stringResource(R.string.facebook),
            tint = Color(teal),
        )
    }
}
