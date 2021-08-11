package com.example.cupofcoffee.app.views.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.views.home.ui.theme.CupOfCoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var data = intent.extras
        if (savedInstanceState != null) {
            data = savedInstanceState
        }
        val post: Post = data?.getParcelable(EXTRA_POST)
            ?: throw IllegalStateException("Post cannot be null")
        setContentView(PostDetailView(this, post))
    }

    companion object {
        private const val EXTRA_POST = "extra_post"
        fun getIntent(context: Context, post: Post): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra(EXTRA_POST, post)
            }
        }
    }
}
