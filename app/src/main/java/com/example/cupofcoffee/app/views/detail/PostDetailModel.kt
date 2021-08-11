package com.example.cupofcoffee.app.views.detail

import asShortName
import com.example.cupofcoffee.IODispatcher
import com.example.cupofcoffee.app.data.models.Comment
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.views.detail.PostDetailAction.InitAction
import com.example.cupofcoffee.app.views.detail.PostDetailAction.ReLoadPostAndComments
import com.example.cupofcoffee.base.ViewState
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class PostDetailModel @Inject constructor(
    private val postRepository: PostRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val log: Log,
) {
    private lateinit var scope: ManagedCoroutineScope
    private lateinit var post: Post

    private var currState = PostDetailViewState(
        subReddit = "",
        postShortName = ""
    )
    private val internalViewState = MutableStateFlow(currState)

    val viewState = internalViewState.asStateFlow()
    val actions = MutableStateFlow<PostDetailAction>(InitAction)

    fun init(scope: ManagedCoroutineScope, post: Post) {
        this.scope = scope
        this.post = post
        actions.onEach {
            log.d("doAction: $it")
            when (it) {
                InitAction -> doInitAction()
                ReLoadPostAndComments -> doReloadPostsAndComments()
            }
        }.launchIn(this.scope)
    }

    private fun doInitAction() {
        if (post.subreddit != null && post.postFullName?.asShortName() != null) {
            currState = currState.copy(
                post = post,
                subReddit = post.subreddit!!,
                postShortName = post.postFullName?.asShortName()!!
            )
            doLoadPostsAndComments()
        } else {
            currState = currState.copy(showLoadingError = true)
            scope.launch { internalViewState.emit(currState) }
        }
    }

    private fun doReloadPostsAndComments() {
        if (currState.subReddit.isEmpty() || currState.postShortName.isEmpty()) {
            scope.launch { internalViewState.emit(currState) }
        } else {
            doLoadPostsAndComments()
        }
    }

    private fun doLoadPostsAndComments() {
        scope.launch {
            currState = currState.copy(isLoadingComments = true)
            internalViewState.emit(currState)
            val result =
                postRepository.getPostDetail(currState.subReddit, currState.postShortName)
            if (result?.post != null) {
                scope.launch(ioDispatcher) {
                    val flatComments = result.comments?.flatten()
                    currState = currState.copy(
                        isLoadingComments = false,
                        showLoadingError = false,
                        post = result.post,
                        comments = flatComments
                    )
                    internalViewState.emit(currState)
                }
            } else {
                currState = currState.copy(isLoadingComments = false, showLoadingError = true)
                internalViewState.emit(currState)
            }
        }
    }

}


data class PostDetailViewState(
    val isLoadingComments: Boolean = false,
    val showLoadingError: Boolean = false,

    val subReddit: String,
    val postShortName: String,

    val post: Post? = null,
    val comments: List<CommentViewData>? = null
) : ViewState()


sealed class PostDetailAction : ViewState() {
    object InitAction : PostDetailAction()
    object ReLoadPostAndComments : PostDetailAction()
}

internal fun List<Comment>?.flatten(): List<CommentViewData> {
    val flatList = arrayListOf<CommentViewData>()
    this?.forEach { comment -> doFlatten(flatList, comment, 0) }
    return flatList
}

private fun doFlatten(flattenList: ArrayList<CommentViewData>, comment: Comment, level: Int) {
    flattenList.add(CommentViewData(comment.copy(repliesResult = null), level))
    comment.replies?.forEach {
        doFlatten(flattenList, it, level + 1)
    }
}

data class CommentViewData(
    val comment: Comment,
    val nesting: Int = 0,
    val isHidden: Boolean = false
)
