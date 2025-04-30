package com.money.expenz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.money.expenz.model.ExpenzAppBar.ExpenzTheme
import com.money.expenz.ui.home.ExpenzViewModel.LoadingState

@Composable
fun LoadingProgressBar(loadingState: LoadingState) {
    if (loadingState == LoadingState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ExpenzTheme.colorScheme.onPrimary),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = ExpenzTheme.colorScheme.primary,
                strokeWidth = 10.dp,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
LoadingProgressBar(loadingState = LoadingState.Loading)
}