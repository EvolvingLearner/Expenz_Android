package com.money.expenz.ui.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.money.expenz.ExpenzApplication
import com.money.expenz.model.ViewModelFactory
import com.money.expenz.ui.theme.ExpenzTheme

class LoginActivity : AppCompatActivity() {
    val loginViewModel: LoginViewModel by viewModels { ViewModelFactory(LoginViewModel::class.java, (application as ExpenzApplication).repository) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       loginViewModel.getDatabaseUsers()
        setContent {
            ExpenzTheme {
                LoginScreen(viewModel = loginViewModel)
            }
        }
    }
}