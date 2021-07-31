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

//        val subReddit: String = data?.getString(EXTRA_SUBREDDIT)
//            ?: throw IllegalStateException("Subreddit cannot be null")
//
//        val postShortName: String = data.getString(EXTRA_POST_SHORT_NAME)
//            ?: throw IllegalStateException("Subreddit cannot be null")

        val post: Post = data?.getParcelable<Post>(EXTRA_POST)
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
