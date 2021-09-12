package com.example.cupofcoffee.app.data.repository

import com.example.cupofcoffee.app.data.models.Comment
import com.example.cupofcoffee.app.data.models.DataChild
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.data.network.RedditService
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.helpers.network.Result
import dagger.hilt.android.scopes.ViewScoped
import javax.inject.Inject

@ViewScoped
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
                logError("getPosts", result.code, result.t)
                result.t?.printStackTrace()
                null
            }
            is Result.NetworkFailure -> {
                logError("getPosts", t = result.t)
                null
            }
        }
    }


    suspend fun getPostDetail(subReddit: String, postShortName: String): PostDetailResult? {
        return when (val result = redditService.getPostDetail(subReddit, postShortName)) {
            is Result.NetworkError -> {
                logError("getPostDetail", result.code, result.t)
                null
            }
            is Result.NetworkFailure -> {
                logError("getPostDetail", t = result.t)
                null
            }
            is Result.Success -> PostDetailResult(
                (result.body.first().data?.children?.first() as DataChild.PostData).data,
                result.body[1].data?.children?.mapNotNull {
                    if (it is DataChild.CommentData) it.data else null
                }
            )
        }
    }


    private fun logError(s: String, code: Int? = -1, t: Throwable?) {
        log.e("NetworkError while $s $code $t")
    }
}

data class PostsResult(val after: String?, val posts: List<Post>?)
data class PostDetailResult(val post: Post?, val comments: List<Comment>?)
