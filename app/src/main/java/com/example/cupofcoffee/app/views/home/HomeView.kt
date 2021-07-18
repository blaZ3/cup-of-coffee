package com.example.cupofcoffee.app.views.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.cupofcoffee.app.data.ImageSource
import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.app.data.PreviewImage
import com.example.cupofcoffee.helpers.coroutine.LifecycleManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import com.google.accompanist.coil.rememberCoilPainter
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors.fromActivity
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class HomeView(context: Context) : AbstractComposeView(context) {

    private lateinit var model: HomeModel
    private lateinit var log: Log

    @Composable
    override fun Content() {
        val entryPoint = fromActivity(
            context as AppCompatActivity,
            HomeViewEntryPoint::class.java
        )
        findViewTreeLifecycleOwner()?.lifecycleScope?.let {
            log = entryPoint.log()
            model = entryPoint.model()
            Home(model.viewState, log)
            model.init(LifecycleManagedCoroutineScope(it))
        }
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface HomeViewEntryPoint {
        fun model(): HomeModel
        fun log(): Log
    }

}

@Composable
@Preview
private fun HomeComposePreview() {
    val post = Post(
        title = "Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 ",
        subreddit = "subreddit",
        authorFullName = "author",
        isOriginalContent = false,
        over18 = true,
        created = 1623015981,
        ups = 10_000L,
        spoiler = true,
        totalAwardsReceived = 10
    )
    val state = MutableStateFlow(
        HomeViewState(
            posts = listOf(
                post,
                post.copy(
                    title = "Title 2",
                    isVideo = false,
                    isOriginalContent = true,
                    preview = com.example.cupofcoffee.app.data.Preview(
                        images = listOf(
                            PreviewImage(
                                source = ImageSource(url = "")
                            )
                        )
                    )
                ),
                post.copy(title = "Title 3"),
                post.copy(title = "Title 4"),
                post.copy(title = "Title 5"),
                post.copy(title = "Title 6"),
                post.copy(title = "Title 7")
            ),
            isLoading = false,
            showLoadingError = false,
            showEmptyPosts = true
        )
    )
    Home(viewState = state)
}


@Composable
private fun Home(viewState: StateFlow<HomeViewState>, log: Log? = null) {
    viewState.collectAsState().value.let { state ->
        if (state.isLoading) {
            Column {
                Text(text = "Loading.....")
            }
            return
        }
        if (state.showLoadingError) {
            Column {
                Text(text = "Error loading posts")
            }
            return
        }
        if (state.showEmptyPosts) {
            Column {
                Text(text = "No posts")
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Reload")
                }
            }
            return
        }
        state.posts?.let {
            LazyColumn { items(it) { post -> Post(post, log) } }
        }
    }
}

@Composable
private fun Post(post: Post, log: Log? = null) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row {
                post.subreddit?.let {
                    Text(
                        text = "r/${post.subreddit}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                if (post.isOriginalContent) {
                    Text(
                        text = "[OC]",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                if (post.over18) {
                    Text(
                        text = "NSFW",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            post.title?.let {
                Text(
                    text = post.title,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            val cleanedUrl = post.preview?.images?.first()?.source?.getCleanedUrl()
            log?.d(cleanedUrl.toString())
            if (post.isImage && cleanedUrl != null) {
                Image(
                    painter = rememberCoilPainter(request = cleanedUrl),
                    contentDescription = post.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = Crop
                )
            }
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = post.created.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}
