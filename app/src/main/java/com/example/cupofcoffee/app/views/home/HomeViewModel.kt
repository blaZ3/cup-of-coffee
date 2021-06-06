package com.example.cupofcoffee.app.views.home

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.helpers.log.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel @Inject constructor(
    private val postRepo: PostRepository,
    private val log: Log
) {

    private var currState = HomeViewState()
    private val internalStateView = MutableStateFlow(currState)

    val viewState = internalStateView.asStateFlow()

    val actions = ConflatedBroadcastChannel<HomeAction>()

    private val subreddit = "popular"


    fun init(lifecycleCoroutineScope: LifecycleCoroutineScope) {

        actions.asFlow().onEach {
            doAction(it)
        }.launchIn(lifecycleCoroutineScope)

        lifecycleCoroutineScope.launchWhenCreated {

            currState = currState.copy(isLoading = true)
            internalStateView.emit(currState)

            postRepo.getPosts(subreddit)?.let {
                log.d(it.toString())
                currState = currState.copy(posts = it, isLoading = false)
                internalStateView.emit(currState)
            }
        }
    }

    private fun doAction(it: HomeAction) {
        when (it) {
            HomeAction.LoadMore -> TODO()
        }
    }

}

data class HomeViewState(
    val isLoading: Boolean = false,
    val posts: List<Post>? = null
)

sealed class HomeAction {
    object LoadMore : HomeAction()
}
