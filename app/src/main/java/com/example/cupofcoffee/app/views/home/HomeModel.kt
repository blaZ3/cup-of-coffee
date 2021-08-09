package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.models.DEFAULT_SUB_REDDIT
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.models.SubReddit
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.data.repository.UserSettingsRepository
import com.example.cupofcoffee.app.views.home.HomeAction.*
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import kotlinx.coroutines.flow.*
import java.lang.System.currentTimeMillis
import javax.inject.Inject


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
            InitAction, UserSettingsChanged -> doInitAction()
            is Reload -> doReload()
            is LoadMore -> doLoadMore()
            is SelectedSubRedditChanged -> doChangeSelectedSubReddit(it.subReddit)
        }
    }

    private fun doChangeSelectedSubReddit(subReddit: SubReddit) {
        scope.launch {
            userSettingsRepository.changeSelectedSubReddit(subReddit)
        }
    }

    private fun doLoadMore() {
        scope.launch {
            currState = currState.copy(isPaginating = true)
            internalViewState.emit(currState)
            loadPosts()
        }
    }

    private fun doReload() {
        scope.launch {
            currState = currState.copy(
                isLoading = true,
                after = null,
                showLoadingError = false,
                showEmptyPosts = false,
                isPaginating = false
            )
            internalViewState.emit(currState)
            loadPosts()
        }
    }

    private fun doInitAction() {
        scope.launch {
            currState = currState.copy(
                isLoading = true,
                showLoadingError = false,
                showEmptyPosts = false,
                isPaginating = false
            )
            internalViewState.emit(currState)

            scope.launch {
                userSettingsRepository.getUserSelectedSubReddit()
                    .collect {
                        currState = currState.copy(
                            selectedSubreddit = it,
                            after = null,
                            isLoading = true
                        )
                        internalViewState.emit(currState)
                        loadPosts(reset = true)
                    }
            }

            scope.launch {
                userSettingsRepository.getUserSubReddits().collect {
                    currState = currState.copy(subReddits = it)
                    internalViewState.emit(currState)
                }
            }

        }
    }

    private fun loadPosts(reset: Boolean = false) {
        log.d("loadPosts")
        if (currState.selectedSubreddit == DEFAULT_SUB_REDDIT) {
            log.d("selectedSubreddit is $DEFAULT_SUB_REDDIT")
            return
        }

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
