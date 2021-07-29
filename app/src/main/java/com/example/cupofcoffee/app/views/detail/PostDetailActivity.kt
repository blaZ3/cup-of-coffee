package com.example.cupofcoffee.app.views.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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

        val subReddit: String = data?.getString(EXTRA_SUBREDDIT)
            ?: throw IllegalStateException("Subreddit cannot be null")

        val postShortName: String = data.getString(EXTRA_POST_SHORT_NAME)
            ?: throw IllegalStateException("Subreddit cannot be null")

        setContentView(PostDetailView(this, subReddit, postShortName))
    }


    companion object {
        private const val EXTRA_SUBREDDIT = "extra_subreddit"
        private const val EXTRA_POST_SHORT_NAME = "extra_post_short_name"
        fun getIntent(context: Context, subreddit: String?, postShortName: String?): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra(EXTRA_SUBREDDIT, subreddit)
                putExtra(EXTRA_POST_SHORT_NAME, postShortName)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CupOfCoffeeTheme {
        Greeting("Android")
    }
}
