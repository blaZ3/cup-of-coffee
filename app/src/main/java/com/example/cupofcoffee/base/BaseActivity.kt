package com.example.cupofcoffee.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_VIEW_STATE = "extra_view_state"

abstract class BaseActivity<T : ViewState> : AppCompatActivity() {

    protected lateinit var view: BaseView<T>
    protected var viewState: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewState = savedInstanceState?.get(EXTRA_VIEW_STATE) as T?
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_VIEW_STATE, view.getCurrentViewState())
        super.onSaveInstanceState(outState)
    }

}
