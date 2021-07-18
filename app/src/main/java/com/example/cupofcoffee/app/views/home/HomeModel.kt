package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.views.home.HomeAction.InitAction
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class HomeModel @Inject constructor(
    private val postRepo: PostRepository,
    private val log: Log
) {
    private lateinit var scope: ManagedCoroutineScope

    private var currState = HomeViewState()
    private val internalStateView = MutableStateFlow(currState)

    val viewState = internalStateView.asStateFlow()
    val actions = MutableStateFlow<HomeAction>(InitAction)

    fun init(lifecycleCoroutineScope: ManagedCoroutineScope) {
        this.scope = lifecycleCoroutineScope
        actions.onEach { doAction(it) }.launchIn(lifecycleCoroutineScope)
    }

    private fun doAction(it: HomeAction) {
        when (it) {
            InitAction -> {
                scope.launch {
                    currState = currState.copy(isLoading = true)
                    internalStateView.emit(currState)
                    val posts = postRepo.getPosts(currState.subreddit)
                    currState = if (posts != null) {
                        currState.copy(posts = posts, isLoading = false,
                            showEmptyPosts = posts.isEmpty())
                    } else {
                        currState.copy(isLoading = false, showLoadingError = true)
                    }
                    internalStateView.emit(currState)
                }
            }
            HomeAction.LoadMore -> TODO()
        }
    }

}

data class HomeViewState(
    val subreddit: String = "popular",
    val isLoading: Boolean = false,
    val showEmptyPosts: Boolean = false,
    val showLoadingError: Boolean = false,
    val posts: List<Post>? = null
)

sealed class HomeAction {
    object InitAction : HomeAction()
    object LoadMore : HomeAction()
}
