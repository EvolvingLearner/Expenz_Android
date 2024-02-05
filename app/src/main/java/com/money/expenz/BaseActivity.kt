package com.money.expenz

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.model.ExpenzAppBar
import com.money.expenz.model.PreferenceManager
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
                    NavigationSetup(viewModel, navController = navController, Screen.Login.route)

                    val preferencesManager =
                        remember { PreferenceManager(application as ExpenzApplication) }
                    val data =
                        remember { mutableStateOf(preferencesManager.getData("Logged_in", false)) }

                    // Use the data variable in your Composable
                    viewModel.isUserLoggedIn.observe(this) { isLoggedIn ->
                        preferencesManager.saveData("Logged_in", isLoggedIn)
                        data.value = isLoggedIn
                    }
                    Log.d("Expenz", "isLoggedIn " + data.value)

                    if (data.value) {
                        navController.navigate(Screen.Home.route)
                        ExpenzAppBar().AppBar(viewModel)
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
