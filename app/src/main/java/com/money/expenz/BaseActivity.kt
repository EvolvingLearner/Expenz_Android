package com.money.expenz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.money.expenz.data.ExpenzAppBar
import com.money.expenz.ui.theme.ExpenzTheme


open class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExpenzTheme {
                ExpenzAppBar().AppBar()
            }
        }
    }

    /*object ExpenzTheme {
        val colorScheme: ColorScheme
            @Composable
            get() = MaterialTheme.colorScheme

        val typography: Typography
            @Composable
            get() = MaterialTheme.typography

    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun AppBar() {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.menu_home)) },
                backgroundColor = Color.Transparent,
                contentColor = ExpenzTheme.colorScheme.onSurfaceVariant,
                actions = {
                    AppBarActionButton(
                        imageVector = Icons.Outlined.ExitToApp,
                        description = "Logout",
                        onClick = { finishAffinity() })
                }
            )
        },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    contentColor = ExpenzTheme.colorScheme.onSurfaceVariant,
                    actions = {
                        AppBarActionButton(onClick = { *//* doSomething() *//* }, imageVector = Icons.Filled.Home, description = "")

                        AppBarActionButton(onClick = { *//* doSomething() *//* }, imageVector = Icons.Filled.List, description = "")
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {*//*TODO*//* },
                            containerColor = ExpenzTheme.colorScheme.inversePrimary
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_button),
                                contentDescription = "Add"
                            )
                        }
                    })
            },
            backgroundColor = ExpenzTheme.colorScheme.surface,
            contentColor = ExpenzTheme.colorScheme.onSurface) { innerPadding ->
            BaseContent(innerPadding)
        }
    }

    @Composable
    fun BaseContent(innerPaddingValues: PaddingValues) {
        val mContext = LocalContext.current
        Column(
            Modifier
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    paddingValues = PaddingValues(
                        10.dp,
                        innerPaddingValues.calculateTopPadding(),
                        10.dp,
                        innerPaddingValues.calculateBottomPadding()
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }

    }

    @Composable
    fun AppBarActionButton(
        imageVector: ImageVector,
        description: String,
        onClick: () -> Unit
    ) {
        IconButton(onClick = {
            onClick()
        }) {
            Icon(imageVector = imageVector, contentDescription = description, tint = ExpenzTheme.colorScheme.primary)
        }
    }

    @Preview
    @Composable
    fun DefaultPreview() {
        ExpenzTheme {
            ExpenzAppBar().AppBar()
        }
    }*/
}