package com.example.cupofcoffee

import android.os.Bundle
import com.example.cupofcoffee.app.views.home.HomeView
import com.example.cupofcoffee.base.BaseActivity
import com.example.cupofcoffee.base.BaseView
import com.example.cupofcoffee.base.ViewState
import com.example.cupofcoffee.helpers.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = (HomeView(this, AppNavigator(this), viewState) as BaseView<ViewState>).also {
            setContentView(it)
        }
    }
}
