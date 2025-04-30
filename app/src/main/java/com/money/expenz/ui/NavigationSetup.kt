package com.money.expenz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.money.expenz.ui.home.ExpenzViewModel
import com.money.expenz.ui.home.HomeScreen

@Composable
fun NavigationSetup(
    viewModel: ExpenzViewModel,
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(viewModel, navController)
        }
        composable(BottomNavItem.Subscriptions.route) {
            viewModel.getSubscriptionList()
            SubscriptionsScreen(viewModel, navController)
        }
        composable(BottomNavItem.Add.route) {
            AddScreen(navController, viewModel)
        }
        composable(Screen.Details.route) {
            DetailsScreen(viewModel, navController)
        }
        composable(Screen.IncomeList.route) {
            viewModel.getUserWithIEDetails()
            DataListScreen(viewModel, navController)
        }
        composable(Screen.ExpenseList.route) {
            viewModel.getUserWithIEDetails()
            DataListScreen(viewModel, navController)
        }
    }
}
