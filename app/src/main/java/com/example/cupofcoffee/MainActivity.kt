package com.example.cupofcoffee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cupofcoffee.app.views.home.HomeView
import com.example.cupofcoffee.helpers.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(HomeView(this, AppNavigator(this)))
    }
}
