package com.money.expenz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.money.expenz.model.ExpenzAppBar
import com.money.expenz.model.ViewModelFactory
import com.money.expenz.ui.home.ExpenzViewModel
import com.money.expenz.ui.login.LoginActivity
import com.money.expenz.ui.theme.ExpenzTheme
import com.money.expenz.utils.ExpenzUtil

open class BaseActivity : ComponentActivity() {
    val viewModel: ExpenzViewModel by viewModels { ViewModelFactory(ExpenzViewModel::class.java,(application as ExpenzApplication).repository) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExpenzTheme {
                val navController = rememberNavController()
                val userId = ExpenzUtil.UserSession.userId ?: 0
                viewModel.getLoggedInUserDetails(userId)
                ExpenzAppBar().AppBar(viewModel, navController,onNavigateToLoginScreen = {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                })
            }
        }
    }
}


