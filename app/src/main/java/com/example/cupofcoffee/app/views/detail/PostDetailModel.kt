package com.example.cupofcoffee.app.views.detail

import android.os.Parcelable
import asShortName
import com.example.cupofcoffee.IODispatcher
import com.example.cupofcoffee.app.data.models.Comment
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.views.detail.PostDetailAction.InitAction
import com.example.cupofcoffee.app.views.detail.PostDetailAction.ReLoadPostAndComments
import com.example.cupofcoffee.base.Action
import com.example.cupofcoffee.base.ViewState
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

internal class PostDetailModel @Inject constructor(
    private val postRepository: PostRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private lateinit var scope: ManagedCoroutineScope

    private var currState = PostDetailViewState()
    private val internalViewState = MutableStateFlow(currState)

    val viewState = internalViewState.asStateFlow()
    val actions = MutableStateFlow<PostDetailAction>(InitAction)

    fun init(
        scope: ManagedCoroutineScope,
        post: Post?,
        savedViewState: PostDetailViewState? = null
    ) {
        if (post == null && savedViewState == null) throw IllegalStateException(
            "Both post and savedViewState cannot be null"
        )
        this.scope = scope
        currState = currState.copy(
            post = post,
            subReddit = post?.subreddit ?: "",
            postShortName = post?.postFullName?.asShortName() ?: ""
        )
        actions.onEach {
            when (it) {
                InitAction -> doInitAction(savedViewState)
                ReLoadPostAndComments -> doReloadPostsAndComments()
            }
        }.launchIn(this.scope)
    }

    private fun doInitAction(savedViewState: PostDetailViewState?) {
        savedViewState?.let {
            scope.launch {
                currState = it
                internalViewState.emit(currState)
            }
            return
        }

        if (
            currState.post?.subreddit.isNullOrEmpty() ||
            currState.post?.postFullName?.asShortName().isNullOrEmpty()
        ) {
            scope.launch {
                currState = currState.copy(showLoadingError = true, isLoadingComments = false)
                internalViewState.emit(currState)
            }
        } else {
            doLoadPostsAndComments()
        }
    }

    private fun doReloadPostsAndComments() {
        if (currState.subReddit.isNullOrEmpty() || currState.postShortName.isNullOrEmpty()) {
            scope.launch {
                currState = currState.copy(showLoadingError = true, isLoadingComments = false)
                internalViewState.emit(currState)
            }
        } else {
            doLoadPostsAndComments()
        }
    }

    private fun doLoadPostsAndComments() {
        scope.launch {
            currState = currState.copy(isLoadingComments = true)
            internalViewState.emit(currState)
            val result =
                postRepository.getPostDetail(
                    currState.subReddit.toString(),
                    currState.postShortName.toString()
                )
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


@Parcelize
data class PostDetailViewState(
    val isLoadingComments: Boolean = false,
    val showLoadingError: Boolean = false,

    val subReddit: String? = null,
    val postShortName: String? = null,

    val post: Post? = null,
    val comments: List<CommentViewData>? = null
) : ViewState()


sealed class PostDetailAction : Action() {
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

@Parcelize
data class CommentViewData(
    val comment: Comment,
    val nesting: Int = 0,
    val isHidden: Boolean = false
) : Parcelable
