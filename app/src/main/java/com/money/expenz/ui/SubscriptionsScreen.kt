package com.money.expenz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.money.expenz.R
import com.money.expenz.data.Subscription
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.home.ExpenzViewModel
import com.money.expenz.ui.theme.Typography

@Composable
fun SubscriptionsScreen(
    viewModel: ExpenzViewModel,
    navController: NavController,
) {
    val subscriptionList by viewModel.subscriptionList.collectAsState()

    val errorState by viewModel.errorState.collectAsState()
    SubscriptionList(subscriptions = subscriptionList, navController)
}

@Composable
fun SubscriptionList(
    subscriptions: List<Subscription>,
    navController: NavController,
) {
    LazyColumn(modifier = Modifier.background(ExpenzTheme.colorScheme.onPrimary)) {
        items(subscriptions) { subscription -> SubscriptionCard(subscription, navController) }
    }
}

@Composable
fun SubscriptionCard(
    subscription: Subscription,
    navController: NavController,
) {
    Row(
        modifier = Modifier.clickable { navController.navigate(Screen.Details.route) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = subscription.category,
                    modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                    style = Typography.bodySmall,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 1.5.em,
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = subscription.date,
                    modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                    style = Typography.bodySmall,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 1.5.em,
                )
            }

            Text(
                text = stringResource(id = R.string.dollar) + subscription.amount.toString(),
                modifier = Modifier.padding(end = 15.dp),
                style = Typography.bodySmall,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 1.5.em,
                textAlign = TextAlign.End,
            )
        }
    }
    Divider()
}
