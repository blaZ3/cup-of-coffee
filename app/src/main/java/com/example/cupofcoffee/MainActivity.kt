package com.example.cupofcoffee

import android.os.Bundle
import com.example.cupofcoffee.app.views.home.HomeView
import com.example.cupofcoffee.app.views.home.HomeViewState
import com.example.cupofcoffee.base.BaseActivity
import com.example.cupofcoffee.helpers.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<HomeViewState>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = HomeView(this, AppNavigator(this), viewState).also {
            setContentView(it)
        }
    }
}
