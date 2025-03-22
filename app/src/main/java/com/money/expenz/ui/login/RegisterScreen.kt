package com.money.expenz.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
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
import com.money.expenz.data.User
import com.money.expenz.model.ExpenzAppBar
import com.money.expenz.utils.ExpenzUtil

@Composable
fun RegisterScreen(viewModel: LoginViewModel, onBackToLogin: () -> Unit) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val viewState by viewModel.viewState.collectAsState(initial = false)
    val context = LocalContext.current
    if (viewState == LoginViewModel.ViewState.LoggedIn) {
        ExpenzUtil.UserSession.userId = viewModel.loggedInUserId
        val intent = Intent(context, BaseActivity::class.java)
        context.startActivity(intent)
    }
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (username.value.isEmpty()) {
                        Toast.makeText(context, "Enter Username", Toast.LENGTH_SHORT).show()
                    } else if (password.value.isEmpty()) {
                        Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show()
                    } else if (email.value.isEmpty()) {
                        Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
                    } else if (country.value.isEmpty()) {
                        Toast.makeText(context, "Select Country", Toast.LENGTH_SHORT).show()
                    } else {
                        val newUser =
                            User(
                                userName = username.value,
                                password = password.value,
                                email = email.value,
                                country = country.value,
                            )
                        viewModel.registerUser(newUser)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ExpenzAppBar.ExpenzTheme.colorScheme.primaryContainer),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 8.dp),
            ) {
                Text(
                    text = "Submit",
                    color = ExpenzAppBar.ExpenzTheme.colorScheme.onPrimaryContainer
                )
            }
            Button(
                onClick = { onBackToLogin() },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ExpenzAppBar.ExpenzTheme.colorScheme.primaryContainer),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 8.dp),
            ) {
                Text(
                    text = "Back to Login",
                    color = ExpenzAppBar.ExpenzTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
    Column(
        modifier =
        Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .background(ExpenzAppBar.ExpenzTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val showPassword by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Register",
            fontSize = 30.sp,
            color = ExpenzAppBar.ExpenzTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Username") },
            value = username.value,
            onValueChange = { username.value = it },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "") },
            trailingIcon = {
                Icon(
                    imageVector = (Icons.Filled.Clear),
                    contentDescription = "",
                    modifier = Modifier.clickable { username.value = "" },
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
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "") },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { password.value = "" },
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "",
                )
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Email") },
            value = email.value,
            onValueChange = { email.value = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "") },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { email.value = "" },
                    imageVector = (Icons.Filled.Clear),
                    contentDescription = "clear",
                )
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Country") },
            value = country.value,
            onValueChange = { country.value = it },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "") },
        )
    }
}

@Preview
@Composable
fun DefaultPreviewRegister() {
    val viewModel: LoginViewModel = viewModel()
    RegisterScreen(viewModel) {}
}
