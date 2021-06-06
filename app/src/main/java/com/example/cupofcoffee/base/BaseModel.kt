package com.example.cupofcoffee.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
abstract class BaseModel {

    abstract val viewState: StateFlow<ViewState>
    abstract val actions: ConflatedBroadcastChannel<Action>

}

//state of the view
abstract class ViewState

//user action
abstract class Action
