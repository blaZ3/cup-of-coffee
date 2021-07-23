package com.example.cupofcoffee.app.data.network

import com.example.cupofcoffee.app.data.PostResult
import com.example.cupofcoffee.base.BaseService
import com.example.cupofcoffee.helpers.network.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface RedditApi {
    @GET("/r/{subreddit}.json")
    suspend fun getPosts(
        @Path("subreddit") subreddit: String,
        @Query("after") after: String?,
    ): Response<PostResult>
}

class RedditService @Inject constructor(private val api: RedditApi) : BaseService() {
    suspend fun getPosts(subreddit: String, after: String?): Result<PostResult> {
        return apiCall { api.getPosts(subreddit, after = after) }
    }
}
