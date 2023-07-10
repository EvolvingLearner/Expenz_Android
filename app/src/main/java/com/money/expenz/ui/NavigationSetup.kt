package com.money.expenz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.money.expenz.ui.home.HomeScreen

@Composable
fun NavigationSetup(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route){
        composable(BottomNavItem.Home.route){
            HomeScreen(navController)
        }
        composable(BottomNavItem.Subscriptions.route) {
            SubscriptionsScreen(navController)
        }
        composable(BottomNavItem.Add.route) {
            AddScreen(navController)
        }
        composable(Screen.Details.route) {
            DetailsScreen()
        }
    }
}