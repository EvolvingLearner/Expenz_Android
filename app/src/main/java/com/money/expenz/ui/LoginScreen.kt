package com.money.expenz.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.home.ExpenzViewModel

@Composable
fun LoginScreen(
    viewModel: ExpenzViewModel,
    navController: NavController,
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Register here"),
            modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { navController.navigate(Screen.Register.route) },
            style =
            TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = ExpenzTheme.colorScheme.onSurface,
            ),
        )
    }
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
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = { },
            style =
            TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                color = ExpenzTheme.colorScheme.onBackground,
            ),
        )
    }
}

@Preview
@Composable
fun DefaultPreviewLogin() {
    val viewModel: ExpenzViewModel = viewModel()
    LoginScreen(viewModel, navController = rememberNavController())
}
