package com.example.cupofcoffee.app.data.repository

import com.example.cupofcoffee.app.data.models.DataChild
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.network.RedditService
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.helpers.network.Result
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val redditService: RedditService,
    private val log: Log
) {

    suspend fun getPosts(subReddit: String, after: String?): PostsResult? {
        return when (val result = redditService.getPosts(subReddit, after)) {
            is Result.Success -> {
                val posts =
                    result.body.data?.children?.filterIsInstance<DataChild.PostData>()
                        ?.mapNotNull { it.data }
                return PostsResult(result.body.data?.after, posts)
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


    suspend fun getComments(subReddit: String, postName: String) {

    }
}

data class PostsResult(val after: String?, val posts: List<Post>?)
