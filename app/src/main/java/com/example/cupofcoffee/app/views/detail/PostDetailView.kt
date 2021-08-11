package com.example.cupofcoffee.app.views.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.cupofcoffee.Error.NetworkError
import com.example.cupofcoffee.R.drawable.ic_back
import com.example.cupofcoffee.R.string.desc_go_back_to_posts
import com.example.cupofcoffee.app.composables.LoadingError
import com.example.cupofcoffee.app.composables.PostDetail
import com.example.cupofcoffee.app.data.models.*
import com.example.cupofcoffee.app.data.models.DataChild.CommentData
import com.example.cupofcoffee.app.views.detail.PostDetailAction.ReLoadPostAndComments
import com.example.cupofcoffee.helpers.coroutine.LifecycleManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.ui.theme.CupOfCoffeeTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors.fromActivity
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import toSubRedditName

@SuppressLint("ViewConstructor")
class PostDetailView(
    context: Context,
    private val post: Post
) : AbstractComposeView(context) {

    private lateinit var log: Log
    private lateinit var scope: LifecycleCoroutineScope
    private lateinit var model: PostDetailModel

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        val entryPoint = fromActivity(
            context as AppCompatActivity,
            PostDetailViewEntryPoint::class.java
        )
        log = entryPoint.log()
        model = entryPoint.postDetailModel()
        findViewTreeLifecycleOwner()?.lifecycleScope?.let {
            scope = it
            model.init(LifecycleManagedCoroutineScope(scope), post)
        }
//        scope.launch {
//            model.actions.emit(ReLoadPostAndComments)
//        }
    }

    @Composable
    override fun Content() {
        PostDetailScreen(
            viewState = model.viewState,
            log = log,
            onReload = {
                scope.launch {
                    model.actions.emit(ReLoadPostAndComments)
                }
            },
            onBack = { (context as PostDetailActivity).finish() }
        )
    }


    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface PostDetailViewEntryPoint {
        fun postDetailModel(): PostDetailModel
        fun log(): Log
    }
}


@Composable
internal fun PostDetailScreen(
    viewState: StateFlow<PostDetailViewState>,
    log: Log? = null,
    onReload: () -> Unit,
    onBack: () -> Unit,
) {
    CupOfCoffeeTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            viewState.collectAsState().value.let { state ->
                log?.d("PostDetailScreen new state: $state")
                TopAppBar(title = { Text(text = state.subReddit.toSubRedditName()) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(id = ic_back),
                                contentDescription = stringResource(desc_go_back_to_posts)
                            )
                        }
                    })
                if (state.showLoadingError || state.post == null) {
                    LoadingError(error = NetworkError, onReload = onReload)
                }
                if (!state.showLoadingError && state.post != null) {
                    val flatComments = state.comments
                    PostDetail(state.post, state.isLoadingComments, flatComments)
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PostDetailScreenPreview() {
    val post = Post(
        title = "Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 Title 1 ",
        subreddit = "subreddit",
        authorFullName = "author",
        isOriginalContent = false,
        over18 = true,
        createdUTC = 1623015981,
        score = 10_000L,
        upvoteRatio = 0.75f,
        spoiler = true,
        totalAwardsReceived = 10
    )
    val comment = Comment()
    val viewState = MutableStateFlow(
        PostDetailViewState(
            post = post,
            comments = (1..10).map {
                comment.copy(
                    body = "Comment $it",
                    authorFullName = "Author name $it",
                    author = "Author name $it",
                    repliesResult = ApiResult(
                        resultType = ResultType.Listing,
                        data = Data(
                            children = (1..5).map { reply ->
                                CommentData(
                                    data = Comment(
                                        authorFullName = "Author name $it-$reply",
                                        author = "Author name $it-$reply",
                                        body = "Reply $reply"
                                    )
                                )
                            }
                        )
                    )
                )
            }.flatten(),
            subReddit = "",
            postShortName = ""
        )
    )
    PostDetailScreen(
        viewState = viewState,
        onBack = {},
        onReload = {}
    )
}
