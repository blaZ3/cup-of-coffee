package com.example.cupofcoffee.helpers.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TestManagedCoroutineScope(private val scope: CoroutineScope) : ManagedCoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext

    override fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        scope.launch(coroutineContext, block = block)
}
