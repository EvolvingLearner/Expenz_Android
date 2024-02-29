package com.money.expenz.ui

import androidx.annotation.StringRes
import com.money.expenz.R

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Login : Screen(Login_Screen, R.string.title_login)
    object Register : Screen(Register_Screen, R.string.title_login)
    object Home : Screen(Home_Screen, R.string.app_name)
    object Add : Screen(Add_Screen, R.string.menu_add)
    object Subscription : Screen(Subscription_Screen, R.string.subscriptions)
    object Details : Screen(Details_Screen, R.string.details)

    companion object {
        fun valueOf(route: String?) =
            when (route) {
                Login_Screen -> Login
                Home_Screen -> Home
                Add_Screen -> Add
                Subscription_Screen -> Subscription
                Details_Screen -> Details
                else -> Home

            }
    }
}
