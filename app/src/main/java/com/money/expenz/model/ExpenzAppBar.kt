package com.money.expenz.model

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.money.expenz.ui.BottomNavItem
import com.money.expenz.ui.ExpenzAlertDialog
import com.money.expenz.ui.NavigationSetup
import com.money.expenz.ui.Screen
import com.money.expenz.ui.home.ExpenzViewModel

class ExpenzAppBar {
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
    fun AppBar(
        viewModel: ExpenzViewModel,
        navController: NavHostController,
        onNavigateToLoginScreen: () -> Unit = {},
    ) {
        // Get current back stack entry
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        Scaffold(
            topBar = {
                TopAppBar(
                    currentScreen = Screen.valueOf(currentRoute ?: Screen.Home.route),
                    canNavigateBack =
                    navController.previousBackStackEntry != null &&
                            !currentRoute.equals(
                                Screen.Home.route,
                            ),
                    navigateUp = { navController.navigateUp() },
                    viewModel = viewModel,
                    onNavigateToLoginScreen = onNavigateToLoginScreen
                )
            },
            bottomBar = {
                BottomNavigationBar(navController = navController,viewModel)
            },
            containerColor = ExpenzTheme.colorScheme.background,
            contentColor = ExpenzTheme.colorScheme.onBackground,
        ) { innerPadding ->
            BaseContent(viewModel, innerPadding, navController)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBar(
        currentScreen: Screen,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: ExpenzViewModel,
        onNavigateToLoginScreen: () -> Unit = {},
    ) {
        ExpenzAlertDialog(viewModel = viewModel)
        CenterAlignedTopAppBar(
            title = { Text(stringResource(currentScreen.title)) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            actions = {
                AppBarActionButton(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    description = "Logout",
                    onClick = {
                        viewModel.showDialog(
                            title = "Confirmation",
                            message = "Are you sure you want to Logout?",
                            onYes = {
                                onNavigateToLoginScreen()
                            },
                            onNo = {
                                viewModel.hideDialog()
                            }
                        )
                    },
                )
            },
            modifier = modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    AppBarActionButton(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        description = "Back",
                        onClick = navigateUp,
                    )
                }
            },
        )
    }

    @Composable
    fun AppBarActionButton(
        imageVector: ImageVector,
        description: String,
        onClick: () -> Unit,
    ) {
        IconButton(onClick = {
            onClick()
        }) {
            Icon(
                imageVector = imageVector,
                contentDescription = description,
                tint = ExpenzTheme.colorScheme.onPrimaryContainer,
            )
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavController, viewModel: ExpenzViewModel) {
        val items =
            listOf(
                BottomNavItem.Home,
                BottomNavItem.Add,
                BottomNavItem.Subscriptions,
            )

        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = ExpenzTheme.colorScheme.primaryContainer,
            contentColor = ExpenzTheme.colorScheme.primary,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(id = item.titleResId),
                            tint = ExpenzTheme.colorScheme.onPrimaryContainer,
                        )
                    },
                    label = { Text(text = stringResource(id = item.titleResId)) },
                    selected = currentRoute == item.route,
                    onClick = { if(item.route == Screen.Add.route) viewModel.iedetailsToEdit = null
                        navigateTo(navController, item.route) },
                )
            }
        }
    }

    @Composable
    fun BaseContent(
        viewModel: ExpenzViewModel,
        innerPaddingValues: PaddingValues,
        navController: NavHostController,
    ) {
        Column(
            Modifier.padding(
                paddingValues =
                PaddingValues(
                    10.dp,
                    innerPaddingValues.calculateTopPadding(),
                    10.dp,
                    innerPaddingValues.calculateBottomPadding(),
                ),
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            NavigationSetup(viewModel, navController = navController, Screen.Home.route)
        }
    }
}

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route) {
            saveState = true
        }
        launchSingleTop = true
    }
}
