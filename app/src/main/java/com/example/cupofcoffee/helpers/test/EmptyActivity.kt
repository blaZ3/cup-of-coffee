package com.example.cupofcoffee.helpers.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import dagger.hilt.android.AndroidEntryPoint


/**
 * This is an empty activity used only for testing individual views
 */

@AndroidEntryPoint
class EmptyActivity : AppCompatActivity() {

    lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout = FrameLayout(this)
        setContentView(frameLayout)
    }


}
