package com.example.cupofcoffee.app.data.repository

import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.app.data.network.RedditService
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.helpers.network.Result
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val redditService: RedditService,
    private val log: Log
) {
    suspend fun getPosts(subReddit: String): List<Post>? {
        return when (val result = redditService.getPosts(subReddit)) {
            is Result.Success -> {
                result.body.data?.children?.mapNotNull { it.post }
            }
            is Result.NetworkError -> {
                log.e("NetworkError while getting posts ${result.code} ${result.t}")
                result.t?.printStackTrace()
                null
            }
            is Result.NetworkFailure -> {
                log.e("NetworkFailure while getting posts ${result.t}")
                null
            }
        }
    }
}
