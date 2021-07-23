package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.views.home.HomeAction.*
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.System.currentTimeMillis
import javax.inject.Inject

internal class HomeModel @Inject constructor(
    private val postRepo: PostRepository,
    private val log: Log
) {
    private lateinit var scope: ManagedCoroutineScope

    private var currState = HomeViewState()
    private val internalViewState = MutableStateFlow(currState)

    val viewState = internalViewState.asStateFlow()
    val actions = MutableStateFlow<HomeAction>(InitAction)

    fun init(lifecycleCoroutineScope: ManagedCoroutineScope) {
        this.scope = lifecycleCoroutineScope
        actions.onEach { doAction(it) }.launchIn(lifecycleCoroutineScope)
    }

    private fun doAction(it: HomeAction) {
        log.d("doAction: $it")
        when (it) {
            InitAction -> loadPosts()
            is Reload -> loadPosts()
            is LoadMore -> {
                scope.launch {
                    currState = currState.copy(isPaginating = true)
                    internalViewState.emit(currState)
                }
            }
        }
    }

    private fun loadPosts() {
        scope.launch {
            currState = currState.copy(
                isLoading = true,
                showLoadingError = false,
                showEmptyPosts = false
            )
            internalViewState.emit(currState)
            val posts = postRepo.getPosts(currState.subreddit)
            currState = currState.copy(isLoading = false)
            currState = if (posts != null) {
                currState.copy(
                    posts = posts,
                    showEmptyPosts = posts.isEmpty()
                )
            } else {
                currState.copy(
                    showLoadingError = true
                )
            }
            internalViewState.emit(currState)
        }
    }

}

data class HomeViewState(
    val subreddit: String = "popular",
    val isLoading: Boolean = false,
    val showEmptyPosts: Boolean = false,
    val showLoadingError: Boolean = false,
    val isPaginating: Boolean = false,
    val posts: List<Post>? = null
)

sealed class HomeAction {
    object InitAction : HomeAction()
    data class Reload(val timestamp: Long = currentTimeMillis()) : HomeAction()
    data class LoadMore(val timestamp: Long = currentTimeMillis()) : HomeAction()
}
