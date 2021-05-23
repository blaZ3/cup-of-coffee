package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.repository.PostRepository
import javax.inject.Inject

class HomeModel @Inject constructor(private val postRepo: PostRepository) {
    suspend fun getPosts(subreddit: String) {
        postRepo.getPosts(subreddit)
    }
}
