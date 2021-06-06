package com.example.cupofcoffee.app.views.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.cupofcoffee.app.data.Post
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalCoroutinesApi::class)
@FlowPreview
class HomeView(context: Context) : AbstractComposeView(context) {

    lateinit var viewModel: HomeViewModel

    @Composable
    override fun Content() {
        val entryPoint = EntryPointAccessors.fromActivity(
            context as AppCompatActivity,
            HomeViewEntryPoint::class.java
        )
        findViewTreeLifecycleOwner()?.lifecycleScope?.let {
            viewModel = entryPoint.model()
            viewModel.init(it)
        }

        Home(viewModel.viewState)
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface HomeViewEntryPoint {
        fun model(): HomeViewModel
    }

}

@Composable
@Preview
private fun HomeComposePreview() {
    val state = MutableStateFlow(
        HomeViewState(
            posts = listOf(
                Post(title = "Title 1"),
                Post(title = "Title 2"),
                Post(title = "Title 3"),
                Post(title = "Title 4"),
                Post(title = "Title 5"),
                Post(title = "Title 6"),
            )
        )
    )
    Home(viewState = state)
}


@Composable
private fun Home(viewState: StateFlow<HomeViewState>) {
    viewState.collectAsState().value.let { state ->
        if (state.isLoading) {
            Column {
                Text(text = "Loading.....")
            }
        } else {
            state.posts?.let {
                LazyColumn {
                    items(it) { post ->
                        post.title?.let { Text(text = post.title) }
                        if (post.isImage) {
                            post.title?.let { Text(text = "Show image here") }
                        }
                    }
                }
            } ?: Column {
                Text(text = "No Posts")
            }
        }
    }
}
