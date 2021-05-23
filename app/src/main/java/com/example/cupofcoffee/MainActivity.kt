package com.example.cupofcoffee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cupofcoffee.app.views.home.HomeView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(HomeView(this))
    }
}
