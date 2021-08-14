package com.example.cupofcoffee.base

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner

abstract class BaseView<T : ViewState>(context: Context) : AbstractComposeView(context) {

    protected lateinit var lifecycleOwner: LifecycleOwner

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        findViewTreeLifecycleOwner()?.let {
            this.lifecycleOwner = it
        }
    }

    abstract fun getCurrentViewState(): T
}
