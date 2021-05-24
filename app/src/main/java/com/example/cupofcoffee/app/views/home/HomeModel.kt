package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.helpers.log.Log
import javax.inject.Inject

class HomeModel @Inject constructor(
    private val postRepo: PostRepository,
    private val log: Log
) {
    suspend fun getPosts(subreddit: String) {
        postRepo.getPosts(subreddit)?.let { log.d(it.toString()) }
    }
}
