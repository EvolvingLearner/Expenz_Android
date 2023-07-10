package com.money.expenz.data

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.money.expenz.ui.BottomNavItem
import com.money.expenz.ui.NavigationSetup
import com.money.expenz.ui.Screen

class ExpenzAppBar() {

    object ExpenzTheme {
        val colorScheme: ColorScheme
            @Composable
            get() = MaterialTheme.colorScheme

        val typography: Typography
            @Composable
            get() = MaterialTheme.typography

    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun AppBar(navController: NavHostController = rememberNavController()) {
        // Get current back stack entry
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        // Get the name of the current screen
        val currentScreen = Screen.valueOf(currentRoute)

        Scaffold(
            topBar = {
                TopAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            },
            backgroundColor = ExpenzTheme.colorScheme.surface,
            contentColor = ExpenzTheme.colorScheme.onSurface
        ) { innerPadding ->
            BaseContent(innerPadding, navController)
        }
    }

    @Composable
    fun TopAppBar(
        currentScreen: Screen,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val localContext = LocalContext.current as Activity
        TopAppBar(
            title = { Text(stringResource(currentScreen.title)) },
            backgroundColor = Color.LightGray,
            contentColor = ExpenzTheme.colorScheme.scrim,
            actions = {
                AppBarActionButton(
                    imageVector = Icons.Outlined.ExitToApp,
                    description = "Logout",
                    onClick = {
                        localContext.finishAffinity()
                    })
            },
            modifier = modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun AppBarActionButton(
        imageVector: ImageVector,
        description: String,
        onClick: () -> Unit
    ) {
        IconButton(onClick = {
            onClick()
        }) {
            Icon(
                imageVector = imageVector,
                contentDescription = description,
                tint = ExpenzTheme.colorScheme.scrim
            )
        }
    }

    @Composable
    fun BottomNavigationBar(
        navController: NavController
    ) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Add,
            BottomNavItem.Subscriptions
        )

        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.LightGray,
            contentColor = ExpenzTheme.colorScheme.scrim,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(id = item.titleResId)
                        )
                    },
                    label = { Text(text = stringResource(id = item.titleResId)) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun BaseContent(innerPaddingValues: PaddingValues, navController: NavHostController) {
        Column(
            Modifier.padding(
                paddingValues = PaddingValues(
                    10.dp,
                    innerPaddingValues.calculateTopPadding(),
                    10.dp,
                    innerPaddingValues.calculateBottomPadding()
                )
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NavigationSetup(navController = navController)
        }
    }
}
