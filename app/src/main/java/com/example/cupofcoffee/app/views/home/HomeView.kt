package com.example.cupofcoffee.app.views.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class HomeView(
    context: Context
) : AbstractComposeView(context) {

    init {
        val entryPoint = EntryPointAccessors.fromActivity(
            context as AppCompatActivity,
            HomeViewEntryPoint::class.java
        )
        MainScope().launch {
            entryPoint.model().getPosts("popular")
        }
    }

    @Composable
    override fun Content() {
        Home()
    }


    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface HomeViewEntryPoint {
        fun model(): HomeModel
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
