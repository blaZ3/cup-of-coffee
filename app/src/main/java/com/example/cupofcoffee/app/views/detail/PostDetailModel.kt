package com.example.cupofcoffee.app.views.detail

import com.example.cupofcoffee.IODispatcher
import com.example.cupofcoffee.app.data.models.Comment
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.models.asShortName
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.views.detail.PostDetailAction.InitAction
import com.example.cupofcoffee.app.views.detail.PostDetailAction.LoadPostAndComments
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class PostDetailModel @Inject constructor(
    private val postRepository: PostRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private lateinit var scope: ManagedCoroutineScope

    private var currState = PostDetailViewState()
    private val internalViewState = MutableStateFlow(currState)

    val viewState = internalViewState.asStateFlow()
    val actions = MutableStateFlow<PostDetailAction>(InitAction)


    fun init(scope: ManagedCoroutineScope, post: Post) {
        this.scope = scope
        actions.onEach { doAction(it) }.launchIn(scope)

        if (post.subreddit == null || post.postFullName?.asShortName() == null) {
            scope.launch {
                currState = currState.copy(showLoadingError = true)
                internalViewState.emit(currState)
            }
        } else {
            scope.launch {
                currState = currState.copy(
                    post = post,
                    subReddit = post.subreddit,
                    postShortName = post.postFullName.asShortName()
                )
                internalViewState.emit(currState)
            }
        }
    }

    private fun doAction(action: PostDetailAction) {
        when (action) {
            InitAction -> {
                // do nothing
            }
            is LoadPostAndComments -> {
                if (currState.subReddit.isEmpty() || currState.postShortName.isEmpty()) return
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
    }

}


data class PostDetailViewState(
    val isLoadingComments: Boolean = false,
    val showLoadingError: Boolean = false,

    val subReddit: String = "",
    val postShortName: String = "",

    val post: Post? = null,
    val comments: List<CommentViewData>? = null
)


sealed class PostDetailAction {
    object InitAction : PostDetailAction()
    object LoadPostAndComments : PostDetailAction()
}

internal fun List<Comment>?.flatten(): List<CommentViewData> {
    val flatList = arrayListOf<CommentViewData>()
    this?.forEach { comment -> doFlatten(flatList, comment, 0) }
    return flatList
}

private fun doFlatten(flattenList: ArrayList<CommentViewData>, comment: Comment, level: Int) {
    flattenList.add(CommentViewData(comment, level))
    comment.replies?.forEach {
        doFlatten(flattenList, it, level + 1)
    }
}

data class CommentViewData(
    val comment: Comment,
    val nesting: Int = 0,
    val isHidden: Boolean = false
)
