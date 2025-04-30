package com.money.expenz.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.money.expenz.ui.home.ExpenzViewModel

@Composable
fun ExpenzAlertDialog(
    viewModel: ExpenzViewModel
) {
    if (viewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDialog() },
            title = {
                Text(viewModel.dialogTitle)
            },
            text = {
                Text(viewModel.dialogMessage)
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onYesClicked?.invoke()
                        viewModel.hideDialog()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.onNoClicked?.invoke()
                        viewModel.hideDialog()
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}