package com.example.cupofcoffee.app.views.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.cupofcoffee.Error.NetworkError
import com.example.cupofcoffee.app.*
import com.example.cupofcoffee.app.data.models.ImageSource
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.models.PreviewImage
import com.example.cupofcoffee.app.views.home.HomeAction.LoadMore
import com.example.cupofcoffee.app.views.home.HomeAction.Reload
import com.example.cupofcoffee.helpers.coroutine.LifecycleManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.helpers.navigation.Navigator
import com.example.cupofcoffee.ui.theme.CupOfCoffeeTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors.fromActivity
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@SuppressLint("ViewConstructor")
class HomeView(
    context: Context,
    private val navigator: Navigator
) : AbstractComposeView(context) {

    private lateinit var model: HomeModel
    private lateinit var log: Log
    private lateinit var scope: LifecycleCoroutineScope

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        val entryPoint = fromActivity(
            context as AppCompatActivity,
            HomeViewEntryPoint::class.java
        )
        log = entryPoint.log()
        model = entryPoint.homeDetail()
        findViewTreeLifecycleOwner()?.lifecycleScope?.let {
            scope = it
            model.init(LifecycleManagedCoroutineScope(scope))
        }
    }

    @Composable
    override fun Content() {
        CupOfCoffeeTheme {
            HomeScreen(model.viewState, log,
                onReloadPosts = {
                    scope.launch { model.actions.emit(Reload()) }
                },
                onPageEndReached = {
                    log.d("onPageEndReached")
                    scope.launch { model.actions.emit(LoadMore()) }
                },
                onPostClicked = {
                    navigator.navigateToPostDetail(it)
                }
            )
        }
    }

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface HomeViewEntryPoint {
        fun homeDetail(): HomeModel
        fun log(): Log
    }
}

@Composable
private fun HomeScreen(
    viewState: StateFlow<HomeViewState>,
    log: Log? = null,
    onReloadPosts: () -> Unit,
    onPageEndReached: () -> Unit,
    onPostClicked: (post: Post) -> Unit
) {
    val state by viewState.collectAsState()
    log?.d("HomeScreen new state: $state")
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (state.isLoading) {
            Loading()
        }
        if (state.showLoadingError) {
            LoadingError(NetworkError, onReload = onReloadPosts)
        }

        if (state.showEmptyPosts) EmptyPosts(onReload = onReloadPosts)

        if (!state.isLoading && !state.showLoadingError && !state.showEmptyPosts) {
            state.posts.let {
                Box {
                    val listState = rememberLazyListState()
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.background)
                    ) {
                        items(it) { post -> Post(post, onPostClicked) }
                    }

                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
                            .mapNotNull { it?.index }
                            .distinctUntilChanged()
                            .collect {
                                if (it >= listState.layoutInfo.totalItemsCount - 1) {
                                    onPageEndReached()
                                }
                            }
                    }

                    if (state.isPaginating) {
                        PaginationIndicator(modifier = Modifier.align(BottomCenter))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
private fun HomeComposePreview() {
    val post = Post(
        title = "Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 ",
        subreddit = "subreddit",
        authorFullName = "author",
        isOriginalContent = false,
        over18 = true,
        createdUTC = 1623015981,
        score = 10_000L,
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
                    preview = com.example.cupofcoffee.app.data.models.Preview(
                        images = listOf(
                            PreviewImage(
                                source = ImageSource(url = "")
                            )
                        )
                    )
                ),
                post.copy(
                    title = "Title 2",
                    isVideo = false,
                    isOriginalContent = true,
                    preview = com.example.cupofcoffee.app.data.models.Preview(
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
            showEmptyPosts = false,
            isPaginating = true
        )
    )
    CupOfCoffeeTheme(darkTheme = true) {
        HomeScreen(
            viewState = state,
            onReloadPosts = {},
            onPageEndReached = {},
            onPostClicked = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeComposePreviewLight() {
    val post = Post(
        title = "Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 ",
        subreddit = "subreddit",
        author = "authorName",
        authorFullName = "t2_dfgdfgf",
        isOriginalContent = false,
        over18 = true,
        createdUTC = 1627786704,
        score = 10_000L,
        upvoteRatio = 0.75f,
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
                    preview = com.example.cupofcoffee.app.data.models.Preview(
                        images = listOf(
                            PreviewImage(
                                source = ImageSource(url = "")
                            )
                        )
                    )
                ),
                post.copy(
                    title = "Title 2",
                    isVideo = false,
                    isOriginalContent = true,
                    preview = com.example.cupofcoffee.app.data.models.Preview(
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
            showEmptyPosts = false,
            isPaginating = true
        )
    )
    CupOfCoffeeTheme(darkTheme = false) {
        HomeScreen(
            viewState = state,
            onReloadPosts = {},
            onPageEndReached = {},
            onPostClicked = {}
        )
    }
}
