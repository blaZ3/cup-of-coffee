package com.example.cupofcoffee.helpers.navigation

import androidx.appcompat.app.AppCompatActivity
import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.app.views.detail.PostDetailActivity

interface Navigator {
    fun navigateToPostDetail(post: Post)
}


class AppNavigator(private val activity: AppCompatActivity) : Navigator {

    override fun navigateToPostDetail(post: Post) {
        activity.startActivity(
            PostDetailActivity.getIntent(
                activity,
                post.subreddit, post.name
            )
        )
    }

}
