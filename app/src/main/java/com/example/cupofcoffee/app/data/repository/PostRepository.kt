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
    suspend fun getPosts(subReddit: String, after: String?): Pair<String?, List<Post>?>? {
        return when (val result = redditService.getPosts(subReddit, after)) {
            is Result.Success -> {
                val posts = result.body.data?.children?.mapNotNull { it.post }
                return Pair(result.body.data?.after, posts)
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
