package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cupofcoffee.Error

@Composable
fun Loading(msg: String? = null) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("loading.json"),
    )
    val progress by animateLottieCompositionAsState(composition)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .height(50.dp)
                .width(150.dp),
            composition = composition,
            progress = progress
        )
        Text(
            text = msg ?: "Loading",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun LoadingError(error: Error, onReload: () -> Unit) {
    when (error) {
        Error.NetworkError -> NetworkError(onReload)
    }
}
