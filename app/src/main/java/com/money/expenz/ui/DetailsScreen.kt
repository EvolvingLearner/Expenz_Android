package com.money.expenz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.money.expenz.R
import com.money.expenz.data.IEDetails
import com.money.expenz.data.asMap
import com.money.expenz.data.toIEDetailsDTO
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.home.ExpenzViewModel

@Composable
fun DetailsScreen(
    viewModel: ExpenzViewModel,
    navController: NavController,
) {
    val map = viewModel.selectedIEDetails.value?.toIEDetailsDTO()?.asMap()
    val list = map?.entries?.toList()
    LoadDetailsFromList(viewModel, list, navController)
}

@Composable
fun LoadDetailsFromList(
    viewModel: ExpenzViewModel,
    list: List<Map.Entry<String, Any?>>?,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.Add.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ExpenzTheme.colorScheme.primaryContainer),
                shape = CutCornerShape(10),
            ) {
                Text(
                    text = stringResource(id = R.string.edit),
                    color = ExpenzTheme.colorScheme.onSurfaceVariant,
                    style = ExpenzTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = {
                    viewModel.showDialog(
                        title = "Confirmation",
                        message = "Are you sure you want to delete this item?",
                        onYes = {
                            viewModel.deleteIE()
                            navController.navigate(Screen.Home.route){
                                popUpTo(BottomNavItem.Home.route) { inclusive = true }
                            }
                        },
                        onNo = {
                            viewModel.hideDialog()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ExpenzTheme.colorScheme.primaryContainer),
                shape = CutCornerShape(10),
            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    color = ExpenzTheme.colorScheme.onSurfaceVariant,
                    style = ExpenzTheme.typography.labelLarge,
                )
            }
            ExpenzAlertDialog(viewModel)
        }
    }
    LazyColumn(modifier = Modifier.background(ExpenzTheme.colorScheme.onPrimary)) {
        if (list != null) {
            items(list) { entry ->
                ShowDetails(viewModel = viewModel, details = entry)
            }
        }
    }
}

@Composable
fun ShowDetails(
    viewModel: ExpenzViewModel,
    details: Map.Entry<String, Any?>
) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = details.key.uppercase(),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp,
            color = ExpenzTheme.colorScheme.secondary,
            textAlign = TextAlign.Start,
        )

        Text(
            text = details.value.toString(),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 25.sp,
            color = ExpenzTheme.colorScheme.tertiary,
            textAlign = TextAlign.End,
        )
    }
    Divider()
}
