package com.money.expenz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.model.ExpenzAppBar
import com.money.expenz.repository.UserRepository
import com.money.expenz.ui.NavigationSetup
import com.money.expenz.ui.Screen
import com.money.expenz.ui.home.ExpenzViewModel
import com.money.expenz.ui.theme.ExpenzTheme

open class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExpenzTheme {

                val owner = LocalViewModelStoreOwner.current

                owner?.let {
                    val viewModel: ExpenzViewModel by viewModels { ViewModelFactory((application as ExpenzApplication).repository) }
                    val navController: NavHostController = rememberNavController()
                    NavigationSetup(viewModel, navController = navController, Screen.Home.route)

                    val viewState by viewModel.viewState.collectAsState(initial = false)
                    when (viewState) {
                        ExpenzViewModel.ViewState.LoggedIn -> {
                            navController.navigate(Screen.Home.route)
                            ExpenzAppBar().AppBar(viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenzViewModel::class.java)) {
            return ExpenzViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
