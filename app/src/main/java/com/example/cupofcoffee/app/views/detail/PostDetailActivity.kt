package com.example.cupofcoffee.app.views.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cupofcoffee.app.views.home.ui.theme.CupOfCoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CupOfCoffeeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }


    companion object {
        private const val EXTRA_SUBREDDIT = "extra_subreddit"
        private const val EXTRA_POST_NAME = "extra_post_name"
        fun getIntent(context: Context, subreddit: String?, name: String?): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra(EXTRA_SUBREDDIT, subreddit)
                putExtra(EXTRA_POST_NAME, name)
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
