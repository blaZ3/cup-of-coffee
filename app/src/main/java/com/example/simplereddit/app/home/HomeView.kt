package com.example.simplereddit.app.home

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class HomeView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    @Composable
    override fun Content() {
        Home()
    }

}

@Composable
@Preview
private fun HomeComposePreview() {
    Home()
}


@Composable
private fun Home() {
    Card {
        Text(text = "Home", modifier = Modifier.padding(4.dp))
    }
}