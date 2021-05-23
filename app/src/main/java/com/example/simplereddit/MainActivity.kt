package com.example.simplereddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplereddit.app.home.HomeView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(HomeView(this))
    }
}