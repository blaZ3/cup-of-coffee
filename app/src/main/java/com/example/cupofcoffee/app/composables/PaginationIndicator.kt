package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun PaginationIndicator(modifier: Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("loading.json"),
    )
    val progress by animateLottieCompositionAsState(composition)

    Card(
        modifier = modifier.padding(8.dp)
    ) {
        LottieAnimation(
            modifier = Modifier
                .height(50.dp)
                .width(150.dp),
            composition = composition,
            progress = progress
        )
    }
}
