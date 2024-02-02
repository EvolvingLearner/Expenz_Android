package com.money.expenz

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.model.ExpenzAppBar
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
                    val viewModel: ExpenzViewModel = viewModel(
                        it,
                        "ExpenzViewModel",
                        ViewModelFactory(
                            LocalContext.current.applicationContext
                                    as Application
                        )
                    )
                    val navController: NavHostController = rememberNavController()
                    NavigationSetup(viewModel,navController = navController,Screen.Login.route)

                    if (viewModel.isUserLoggedIn.collectAsState().value) {
                        navController.navigate(Screen.Home.route)
                        ExpenzAppBar().AppBar(viewModel)
                        val currentDBPath = getDatabasePath("user_database").absolutePath
                    }
                }
            }
        }
    }
}

class ViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenzViewModel(application) as T
    }
}
