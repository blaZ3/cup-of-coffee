package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.models.SubReddit
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.data.repository.UserSettingsRepository
import com.example.cupofcoffee.app.views.home.HomeAction.*
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.System.currentTimeMillis
import javax.inject.Inject


private val DEFAULT_SUB_REDDIT = SubReddit("popular")

internal class HomeModel @Inject constructor(
    private val postRepo: PostRepository,
    private val userSettingsRepository: UserSettingsRepository,
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
            InitAction, UserSettingsChanged -> {
                scope.launch {
                    currState = currState.copy(
                        isLoading = true,
                        showLoadingError = false,
                        showEmptyPosts = false,
                        isPaginating = false
                    )
                    internalViewState.emit(currState)

                    val settings = userSettingsRepository.getUserSettings()

                    currState = currState.copy(
                        selectedSubreddit = settings.selectedSubReddit ?: DEFAULT_SUB_REDDIT,
                        subReddits = settings.subReddits
                    )
                    loadPosts()
                }
            }
            is Reload -> {
                scope.launch {
                    currState = currState.copy(
                        isLoading = true,
                        showLoadingError = false,
                        showEmptyPosts = false,
                        isPaginating = false
                    )
                    internalViewState.emit(currState)
                    loadPosts()
                }
            }
            is LoadMore -> {
                scope.launch {
                    currState = currState.copy(isPaginating = true)
                    internalViewState.emit(currState)
                    loadPosts()
                }
            }
            is SelectedSubRedditChanged -> {
                scope.launch {
                    currState = currState.copy(
                        selectedSubreddit = it.subReddit,
                        after = null,
                        isLoading = true
                    )
                    internalViewState.emit(currState)
                    loadPosts(reset = true)
                }
            }
        }
    }

    private fun loadPosts(reset: Boolean = false) {
        log.d("loadPosts")
        scope.launch {
            val result = postRepo.getPosts(
                currState.selectedSubreddit.name,
                currState.after
            )
            val resultPosts = result?.posts
            currState = currState.copy(
                isLoading = false,
                isPaginating = false
            )
            currState = if (resultPosts != null) {
                val newPosts = currState.posts.toMutableList()
                if (reset) newPosts.clear()
                newPosts.addAll(resultPosts)
                currState.copy(
                    posts = newPosts,
                    after = result.after,
                    showEmptyPosts = resultPosts.isEmpty()
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
    val selectedSubreddit: SubReddit = DEFAULT_SUB_REDDIT,
    val subReddits: List<SubReddit> = listOf(),
    val isLoading: Boolean = false,
    val showEmptyPosts: Boolean = false,
    val showLoadingError: Boolean = false,
    val isPaginating: Boolean = false,
    val posts: List<Post> = arrayListOf(),
    val after: String? = null
)

sealed class HomeAction {
    object InitAction : HomeAction()
    object UserSettingsChanged : HomeAction()
    data class Reload(val timestamp: Long = currentTimeMillis()) : HomeAction()
    data class LoadMore(val timestamp: Long = currentTimeMillis()) : HomeAction()
    data class SelectedSubRedditChanged(val subReddit: SubReddit) : HomeAction()
}
