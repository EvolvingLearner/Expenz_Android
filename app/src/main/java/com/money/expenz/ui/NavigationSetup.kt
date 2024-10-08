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
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(viewModel, navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(viewModel)
        }
        composable(BottomNavItem.Home.route) {
            HomeScreen(viewModel, navController, onNavigateToLoginScreen = {
                navController.navigate(Screen.Login.route)
            })
        }
        composable(BottomNavItem.Subscriptions.route) {
            SubscriptionsScreen(navController)
        }
        composable(BottomNavItem.Add.route) {
            AddScreen(navController, viewModel)
        }
        composable(Screen.Details.route) {
            DetailsScreen()
        }
    }
}
