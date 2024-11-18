package com.money.expenz.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.money.expenz.R
import com.money.expenz.data.User
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.Screen
import com.money.expenz.utils.ExpenzUtil.Companion.EXPENSE
import com.money.expenz.utils.ExpenzUtil.Companion.INCOME
import com.money.expenz.utils.ExpenzUtil.Companion.SUBSCRIPTION

@Composable
fun HomeScreen(
    viewModel: ExpenzViewModel,
    navController: NavController,
    onNavigateToLoginScreen: () -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState(initial = false)
    when (viewState) {
        ExpenzViewModel.ViewState.NotLoggedIn -> {
            LaunchedEffect(viewState) {
                onNavigateToLoginScreen()
            }
        }

        ExpenzViewModel.ViewState.LoggedIn -> {
            val user = viewModel.loggedInUser.observeAsState().value
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (user != null) {
                    PieChart(
                        data = mapOf(
                            Pair(INCOME, user.totalIncome.toInt()),
                            Pair(EXPENSE, user.totalExpense.toInt()),
                            Pair(SUBSCRIPTION, 80)
                        )
                    )
                    TotalIncomeExpenseCard(navController, user, viewModel)
                }
            }
        }
    }
}

@Composable
fun TotalIncomeExpenseCard(
    navController: NavController,
    user: User,
    viewModel: ExpenzViewModel,
) {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
    ) {
        Card(
            modifier =
            Modifier
                .height(150.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    navController.navigate(Screen.DataList.route)
                    viewModel.filterIEList("Income")
                },
            elevation = 10.dp,
            backgroundColor = ExpenzTheme.colorScheme.primaryContainer,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.total_income),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 25.sp,
                    color = ExpenzTheme.colorScheme.onPrimaryContainer,
                )

                Text(
                    text = stringResource(id = R.string.dollar) + user.totalIncome.toString(),
                    modifier = Modifier.padding(top = 25.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    color = ExpenzTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Card(
            modifier =
            Modifier
                .padding(5.dp)
                .height(150.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    navController.navigate(Screen.DataList.route)
                    viewModel.filterIEList("Expense")
                },
            elevation = 10.dp,
            backgroundColor = ExpenzTheme.colorScheme.primaryContainer,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.total_Expense),
                    modifier = Modifier.padding(10.dp),
                    style = ExpenzTheme.typography.headlineMedium,
                    fontSize = 25.sp,
                    color = ExpenzTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = stringResource(id = R.string.dollar) + user.totalExpense.toString(),
                    modifier = Modifier.padding(top = 25.dp),
                    style = ExpenzTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    color = ExpenzTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}
