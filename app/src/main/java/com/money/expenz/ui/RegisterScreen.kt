package com.money.expenz.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.money.expenz.data.User
import com.money.expenz.model.ExpenzAppBar
import com.money.expenz.ui.home.ExpenzViewModel

@Composable
fun RegisterScreen(viewModel: ExpenzViewModel) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Button(
            onClick = {
                if (username.value.isEmpty()) Toast.makeText(context, "Enter Username", Toast.LENGTH_SHORT).show()
                else if (password.value.isEmpty()) Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show()
                else if (email.value.isEmpty()) Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
                else if (country.value.isEmpty()) {
                    Toast.makeText(context, "Select Country", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(
                        userName = username.value,
                        password = password.value,
                        email = email.value,
                        country = country.value
                    )
                    viewModel.registerUser(newUser)
                    viewModel.checkUserInDB(username.value,password.value)
                }
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ExpenzAppBar.ExpenzTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Register", color = ExpenzAppBar.ExpenzTheme.colorScheme.onPrimaryContainer)
        }
    }
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .background(ExpenzAppBar.ExpenzTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val showPassword by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Register",
            fontSize = 30.sp,
            color = ExpenzAppBar.ExpenzTheme.colorScheme.onSurface
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
                    modifier = Modifier.clickable { username.value =  "" },
                )
            })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Password") },
            value = password.value,
            visualTransformation = if (showPassword) {
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
                    modifier = Modifier.clickable { password.value =  "" },
                    imageVector = Icons.Filled.Clear,
                    contentDescription = ""
                )
            }
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
                    modifier = Modifier.clickable { email.value =  "" },
                    imageVector = (Icons.Filled.Clear),
                    contentDescription = "clear"
                )
            })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Country") },
            value = country.value,
            onValueChange = { country.value = it },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "") })

    }
}

@Preview
@Composable
fun DefaultPreviewRegister() {
    val viewModel: ExpenzViewModel = viewModel()
    RegisterScreen(viewModel)
}