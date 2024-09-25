package com.money.expenz.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.R
import com.money.expenz.data.Subscription
import com.money.expenz.data.User
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.Screen
import com.money.expenz.ui.theme.Black
import com.money.expenz.ui.theme.Pink80
import com.money.expenz.ui.theme.PurpleGrey80
import com.money.expenz.ui.theme.Typography
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
                Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (user != null) {
                    PieChart(navController, user, viewModel)
                }
            }
        }
    }
}

@Composable
internal fun PieChart(
    navController: NavController,
    user: User,
    viewModel: ExpenzViewModel,
) {
    val chartColors =
        listOf(
            PurpleGrey80,
            Black,
            Pink80,
        )

    val chartValues = listOf(90f, 80f, 40f)

    PieChart(
        viewModel = viewModel,
        navController = navController,
        user = user,
        modifier = Modifier.padding(7.dp),
        colors = chartColors,
        inputValues = chartValues,
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun PieChart(
    viewModel: ExpenzViewModel,
    navController: NavController,
    user: User,
    modifier: Modifier = Modifier,
    colors: List<Color>,
    inputValues: List<Float>,
    legend: List<String> = listOf(INCOME, EXPENSE, SUBSCRIPTION),
) {
    val chartDegrees = 360f // circle shape

    // start drawing clockwise (top to right)
    var startAngle = 270f

    // calculate each input percentage
    val proportions =
        inputValues.map {
            it * 100 / inputValues.sum()
        }

    // calculate each input slice degrees
    val angleProgress =
        proportions.map { prop ->
            chartDegrees * prop / 100
        }

    BoxWithConstraints(modifier = modifier) {
        Canvas(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(310.dp)
                .padding(start = 20.dp, end = 20.dp),
        ) {
            angleProgress.forEachIndexed { index, angle ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = angle,
                    useCenter = true,
                    size = size,
                    style = Fill,
                )
                startAngle += angle
            }
        }
    }
    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
        for (i in inputValues.indices) {
            DisplayLegend(color = colors[i], legend = legend[i])
        }
    }
    TotalIncomeExpenseCard(navController, user, viewModel)
}

@Composable
fun DisplayLegend(
    color: Color,
    legend: String,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Divider(
            modifier = Modifier.width(16.dp),
            thickness = 4.dp,
            color = color,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = legend,
            color = ExpenzTheme.colorScheme.onBackground,
        )
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
    // SubscriptionList(subscriptions = DataSource().loadSubscriptions(), navController)
}

@Composable
fun SubscriptionList(
    subscriptions: List<Subscription>,
    navController: NavController,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.subscriptions),
            modifier =
            Modifier
                .padding(5.dp)
                .align(Alignment.CenterStart),
            style = ExpenzTheme.typography.headlineLarge,
            fontSize = 20.sp,
            color = ExpenzTheme.colorScheme.onSurface,
        )
        IconButton(
            modifier =
            Modifier
                .padding(5.dp)
                .align(Alignment.CenterEnd),
            onClick = {
                navController.navigate(Screen.Subscription.route)
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "",
                tint = ExpenzTheme.colorScheme.onBackground,
            )
        }
    }

    LazyRow {
        items(subscriptions) { subscription -> SubscriptionCard(subscription) }
    }
}

@Composable
fun SubscriptionCard(
    subscription: Subscription,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
        modifier
            .padding(end = 15.dp, top = 5.dp, bottom = 10.dp)
            .width(180.dp)
            .height(70.dp),
        elevation = 8.dp,
        backgroundColor = ExpenzTheme.colorScheme.secondary,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                modifier = modifier.padding(5.dp),
                painter = painterResource(subscription.imageResourceId),
                contentDescription = stringResource(id = R.string.amazon),
                tint = ExpenzTheme.colorScheme.onSecondary,
            )
            Text(
                text = stringResource(subscription.stringResourceId),
                modifier = modifier.padding(10.dp),
                style = Typography.bodySmall,
                fontSize = 20.sp,
                color = ExpenzTheme.colorScheme.onSecondary,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 1.5.em,
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Column(
        Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val viewModel: ExpenzViewModel = viewModel()
        PieChart(rememberNavController(), User(), viewModel)
    }
}
