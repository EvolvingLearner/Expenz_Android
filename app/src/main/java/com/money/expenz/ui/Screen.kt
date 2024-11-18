package com.money.expenz.ui

import androidx.annotation.StringRes
import com.money.expenz.R
import com.money.expenz.utils.Add_Screen
import com.money.expenz.utils.Data_List_Screen
import com.money.expenz.utils.Details_Screen
import com.money.expenz.utils.Home_Screen
import com.money.expenz.utils.Login_Screen
import com.money.expenz.utils.Register_Screen
import com.money.expenz.utils.Subscription_Screen

sealed class Screen(
    val route: String,
    @StringRes val title: Int,
) {
    data object Login : Screen(Login_Screen, R.string.title_login)

    data object Register : Screen(Register_Screen, R.string.title_login)

    data object Home : Screen(Home_Screen, R.string.app_name)

    data object Add : Screen(Add_Screen, R.string.menu_add)

    data object Subscription : Screen(Subscription_Screen, R.string.subscriptions)

    data object Details : Screen(Details_Screen, R.string.details)

    data object DataList : Screen(Data_List_Screen, R.string.data_list)

    companion object {
        fun valueOf(route: String?) =
            when (route) {
                Login_Screen -> Login
                Register_Screen -> Register
                Home_Screen -> Home
                Add_Screen -> Add
                Subscription_Screen -> Subscription
                Data_List_Screen -> DataList
                Details_Screen -> Details
                else -> Home
            }
    }
}
