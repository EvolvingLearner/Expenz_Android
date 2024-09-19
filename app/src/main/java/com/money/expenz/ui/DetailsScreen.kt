package com.money.expenz.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.money.expenz.R
import com.money.expenz.model.ExpenzAppBar

@Composable
fun DetailsScreen() {
    Row(modifier = Modifier.padding(5.dp)) {
        Text(
            text = stringResource(id = R.string.total_income),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 25.sp,
            color = ExpenzAppBar.ExpenzTheme.colorScheme.secondary
        )

        Text(
            text = stringResource(id = R.string.dollar),
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 25.sp,
            color = ExpenzAppBar.ExpenzTheme.colorScheme.tertiary
        )
    }
    Divider()
}
