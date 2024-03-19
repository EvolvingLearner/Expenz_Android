package com.money.expenz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.money.expenz.data.DataSource
import com.money.expenz.data.Subscription
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.theme.Typography

@Composable
fun SubscriptionsScreen(navController: NavController) {
    SubscriptionList(subscriptions = DataSource().loadSubscriptions(), navController)
}

@Composable
fun SubscriptionList(subscriptions: List<Subscription>, navController: NavController) {
    LazyColumn(modifier = Modifier.background(ExpenzTheme.colorScheme.onPrimary)) {
        items(subscriptions) { subscription -> SubscriptionCard(subscription, navController) }
    }
}

@Composable
fun SubscriptionCard(subscription: Subscription, navController: NavController) {
    Row(
        modifier = Modifier.clickable { navController.navigate(Screen.Details.route) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(subscription.imageResourceId),
            contentDescription = null,
            modifier = Modifier.padding(5.dp),
            contentScale = ContentScale.Inside
        )
        Text(
            text = stringResource(subscription.stringResourceId),
            modifier = Modifier.padding(10.dp),
            style = Typography.bodySmall,
            fontSize = 25.sp,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 1.5.em
        )
    }
    Divider()
}
