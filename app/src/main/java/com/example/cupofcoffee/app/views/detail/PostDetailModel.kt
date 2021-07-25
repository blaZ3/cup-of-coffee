package com.example.cupofcoffee.app.views.detail

import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.views.detail.PostDetailAction.InitAction
import com.example.cupofcoffee.helpers.coroutine.ManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class PostDetailModel @Inject constructor(
    private val log: Log,
    private val postRepo: PostRepository
) {

    private lateinit var scope: ManagedCoroutineScope

    private var currState = PostDetailViewState()
    private val internalViewState = MutableStateFlow(currState)

    val viewState = internalViewState.asStateFlow()
    val actions = MutableStateFlow<PostDetailAction>(InitAction)


    fun init(scope: ManagedCoroutineScope) {
        this.scope = scope
        actions.onEach { doAction(it) }.launchIn(scope)
    }

    private fun doAction(it: PostDetailAction) {
        when (it) {
            InitAction -> loadComments()
        }
    }


    private fun loadComments(){

    }

}


data class PostDetailViewState(
    val isLoadingComments: Boolean = false
)


sealed class PostDetailAction {
    object InitAction : PostDetailAction()
}
