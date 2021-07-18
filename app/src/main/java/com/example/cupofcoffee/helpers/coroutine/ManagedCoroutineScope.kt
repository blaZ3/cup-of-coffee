package com.example.cupofcoffee.helpers.coroutine

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

internal interface ManagedCoroutineScope : CoroutineScope {
    fun launch(block: suspend CoroutineScope.() -> Unit): Job
}

internal class LifecycleManagedCoroutineScope(
    private val lifecycleCoroutineScope: LifecycleCoroutineScope
) : ManagedCoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = lifecycleCoroutineScope.coroutineContext

    override fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleCoroutineScope.launchWhenCreated(block)
}
