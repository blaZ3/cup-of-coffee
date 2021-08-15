package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.cupofcoffee.R

@Composable
fun EmptyPosts(onReload: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_posts),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
        Button(onClick = onReload) {
            Text(text = stringResource(R.string.reload_posts))
        }
    }
}
