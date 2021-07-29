package com.example.cupofcoffee.app.views.detail

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.cupofcoffee.Error.NetworkError
import com.example.cupofcoffee.app.Loading
import com.example.cupofcoffee.app.LoadingError
import com.example.cupofcoffee.app.PostDetail
import com.example.cupofcoffee.app.data.models.Comment
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.views.detail.PostDetailAction.LoadPostAndComments
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

class PostDetailView(
    context: Context,
    private val subReddit: String,
    private val postShortName: String
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
            model.init(LifecycleManagedCoroutineScope(scope))
        }
        scope.launch {
            model.actions.emit(LoadPostAndComments(subReddit, postShortName))
        }
    }

    @Composable
    override fun Content() {
        PostDetailScreen(viewState = model.viewState, log = log, onReload = {
            scope.launch {
                model.actions.emit(LoadPostAndComments(subReddit, postShortName))
            }
        })
    }


    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface PostDetailViewEntryPoint {
        fun postDetailModel(): PostDetailModel
        fun log(): Log
    }
}


@Composable
fun PostDetailScreen(
    viewState: StateFlow<PostDetailViewState>,
    log: Log? = null,
    onReload: () -> Unit
) {
    CupOfCoffeeTheme {
        viewState.collectAsState().value.let { state ->
            Column(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    Loading()
                }
                if (state.showLoadingError || state.post == null) {
                    LoadingError(error = NetworkError, onReload = onReload)
                }
                if (!state.isLoading && !state.showLoadingError && state.post != null) {
                    PostDetail(state.post, state.comments)
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
        created = 1623015981,
        ups = 10_000L,
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
                    author = "Author name $it"
                )
            }
        )
    )
    PostDetailScreen(viewState = viewState) {

    }
}
