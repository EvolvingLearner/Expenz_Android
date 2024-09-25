package com.money.expenz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.R
import com.money.expenz.data.IEDetails
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.home.ExpenzViewModel
import com.money.expenz.ui.theme.Typography

@Composable
fun DataListScreen(
    viewModel: ExpenzViewModel,
    navController: NavController,
) {
    if (viewModel.ieDetailsList.isNotEmpty()) {
        DataList(viewModel, viewModel.ieDetailsList, navController = navController)
    } else {
        EmptyMessage()
    }
}

@Composable
fun EmptyMessage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No List to show",
            modifier = Modifier.padding(10.dp),
            style = Typography.bodySmall,
            fontSize = 25.sp,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 1.5.em,
        )
    }
}

@Composable
fun DataList(
    viewModel: ExpenzViewModel,
    ieDetails: List<IEDetails>,
    navController: NavController,
) {
    LazyColumn(modifier = Modifier.background(ExpenzTheme.colorScheme.surfaceVariant)) {
        items(ieDetails) { details -> DataCard(viewModel, details, navController) }
    }
}

@Composable
fun DataCard(
    viewModel: ExpenzViewModel,
    user: IEDetails,
    navController: NavController,
) {
    OutlinedCard(
        modifier = Modifier
            .clickable {
                viewModel.getIEDetails(user.ieId)
                navController.navigate(Screen.Details.route)
            }
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.category,
                    modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                    style = Typography.bodySmall,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 1.5.em,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = user.date,
                    modifier = Modifier.padding(start = 15.dp, bottom = 10.dp),
                    style = Typography.bodySmall,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 1.5.em,
                )
            }

            Text(
                text = stringResource(id = R.string.dollar) + user.amount.toString(),
                modifier = Modifier.padding(end = 15.dp),
                style = Typography.bodySmall,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 1.5.em,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
fun DataListScreenPreview() {
    val viewModel: ExpenzViewModel = viewModel()
    DataListScreen(viewModel, rememberNavController())
}
