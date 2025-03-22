package com.money.expenz.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.money.expenz.BaseActivity
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.utils.ExpenzUtil

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var showRegisterScreen by remember { mutableStateOf(false) }
    val viewState by viewModel.viewState.collectAsState(initial = false)
    val context = LocalContext.current

    if (viewState == LoginViewModel.ViewState.LoggedIn) {
        ExpenzUtil.UserSession.userId = viewModel.loggedInUserId
        val intent = Intent(context, BaseActivity::class.java)
        context.startActivity(intent)
    }

    if (showRegisterScreen) {
        RegisterScreen(viewModel = viewModel){showRegisterScreen = false}
    } else {
        Column(
            modifier =
            Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val showPassword by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Login",
                fontSize = 30.sp,
                color = ExpenzTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Username") },
                value = username.value,
                onValueChange = { username.value = it },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = (Icons.Filled.AccountBox),
                        contentDescription = "",
                    )
                },
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                value = password.value,
                visualTransformation =
                if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it },
                trailingIcon = {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "")
                },
            )

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {
                Button(
                    onClick = {
                        if (username.value.isEmpty() || password.value.isEmpty()) {
                            Toast.makeText(context, "Enter valid data", Toast.LENGTH_SHORT).show()
                        } else if (!viewModel.checkUserInDB(username.value, password.value)) {
                            Toast.makeText(context, "Invalid user", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenzTheme.colorScheme.primaryContainer),
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                ) {
                    Text(text = "Login", color = ExpenzTheme.colorScheme.onPrimaryContainer)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            Box(modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {
                Button(
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenzTheme.colorScheme.primaryContainer),
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = { showRegisterScreen = true },
                ){
                    Text(text = "Register", color = ExpenzTheme.colorScheme.onPrimaryContainer)
                }
            }

        }
    }
}

@Preview
@Composable
fun DefaultPreviewLogin() {
    val viewModel: LoginViewModel = viewModel()
    LoginScreen(viewModel)
}
