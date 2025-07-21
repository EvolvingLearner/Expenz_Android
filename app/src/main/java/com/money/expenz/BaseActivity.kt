package com.money.expenz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.amplitude.android.events.Identify
import com.money.expenz.model.ExpenzAppBar
import com.money.expenz.model.ViewModelFactory
import com.money.expenz.ui.LoadingProgressBar
import com.money.expenz.ui.home.ExpenzViewModel
import com.money.expenz.ui.login.LoginActivity
import com.money.expenz.ui.theme.ExpenzTheme
import com.money.expenz.utils.ExpenzUtil

open class BaseActivity : ComponentActivity() {
    val viewModel: ExpenzViewModel by viewModels {
        ViewModelFactory(
            ExpenzViewModel::class.java,
            (application as ExpenzApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExpenzTheme {
                val navController = rememberNavController()
                val userId = ExpenzUtil.UserSession.userId ?: 0
                val loadingState = viewModel.progressBarLoadingState.collectAsState()
                val user = viewModel.user.collectAsState()
                viewModel.getLoggedInUserDetails(userId)
                when (loadingState.value) {
                    ExpenzViewModel.LoadingState.Loading -> {
                        // Show progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingProgressBar(loadingState = loadingState.value)
                        }
                    }

                    ExpenzViewModel.LoadingState.Success -> {
                        user.value?.let {
                            // Set Amplitude data once user is ready
                            val identify = Identify()
                                .set("name", it.userName)
                                .set("email", it.email)
                            ExpenzApplication.amplitudeInstance.setUserId(userId.toString())
                            ExpenzApplication.amplitudeInstance.identify(identify)

                            // Login UI
                            ExpenzAppBar().AppBar(
                                viewModel,
                                navController,
                                onNavigateToLoginScreen = {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                })
                        } ?: run {
                            // fallback UI if user is null
                            Text("User not found.")
                        }
                    }

                    else -> {
                        // optional error or empty state
                        Text("Loading...")
                    }
                }
            }
        }
    }
}


