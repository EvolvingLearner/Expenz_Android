package com.money.expenz.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = Screen.Home.route,
        titleResId = Screen.Home.title,
        icon = Icons.Default.Home
    )

    data object Add : BottomNavItem(
        route = Screen.Add.route,
        titleResId = Screen.Add.title,
        icon = Icons.Default.AddCircle
    )

    data object Subscriptions : BottomNavItem(
        route = Screen.Subscription.route,
        titleResId = Screen.Subscription.title,
        icon = Icons.AutoMirrored.Filled.List
    )
}
