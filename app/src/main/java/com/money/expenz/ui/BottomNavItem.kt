package com.money.expenz.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.money.expenz.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        titleResId = Screen.Home.title,
        icon = Icons.Default.Home
    )

    object Add : BottomNavItem(
        route = Screen.Add.route,
        titleResId = Screen.Add.title,
        icon = Icons.Default.AddCircle
    )

    object Subscriptions : BottomNavItem(
        route = Screen.Subscription.route,
        titleResId = Screen.Subscription.title,
        icon = Icons.Default.List
    )
}
