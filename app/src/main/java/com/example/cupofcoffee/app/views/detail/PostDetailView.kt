package com.example.cupofcoffee.app.views.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.cupofcoffee.helpers.coroutine.LifecycleManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors.fromActivity
import dagger.hilt.android.components.ActivityComponent

class PostDetailView(context: Context) : AbstractComposeView(context) {

    private lateinit var log: Log
    private lateinit var scope: LifecycleCoroutineScope
    private lateinit var model: PostDetailModel

    @Composable
    override fun Content() {
        val entryPoint = fromActivity(
            context as AppCompatActivity,
            PostDetailViewEntryPoint::class.java
        )
        log = entryPoint.log()
        model = entryPoint.postDetailModel()
        findViewTreeLifecycleOwner()?.lifecycleScope?.let {
            scope = it
            model.init(LifecycleManagedCoroutineScope(scope))
        }
    }


    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface PostDetailViewEntryPoint {
        fun postDetailModel(): PostDetailModel
        fun log(): Log
    }
}
