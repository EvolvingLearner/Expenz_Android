package com.money.expenz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.money.expenz.data.asSubMap
import com.money.expenz.data.toSubDetailsDTO
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.home.ExpenzViewModel

@Composable
fun SubscriptionDetailsScreen(
    viewModel: ExpenzViewModel,
    navController: NavController,
) {
    val map = viewModel.selectedSubscriptionDetails.value?.toSubDetailsDTO()?.asSubMap()
    val list = map?.entries?.toList()
    LoadList(viewModel, list, navController)
}

@Composable
fun LoadList(
    viewModel: ExpenzViewModel,
    list: List<Map.Entry<String, Any?>>?,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
    LazyColumn {
        if (list != null) {
            items(list) { entry ->
                ShowSubscriptionDetails(viewModel = viewModel, details = entry)
            }
        }
    }
}

@Composable
fun ShowSubscriptionDetails(
    viewModel: ExpenzViewModel,
    details: Map.Entry<String, Any?>
) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = details.key.uppercase(),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp,
            color = ExpenzTheme.colorScheme.secondary,
            textAlign = TextAlign.Start,
        )

        Text(
            text = details.value.toString(),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 25.sp,
            color = ExpenzTheme.colorScheme.tertiary,
            textAlign = TextAlign.End,
        )
    }
    Divider()
}
