package com.example.cupofcoffee.app.data.network

import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.helpers.network.BaseService
import com.example.cupofcoffee.helpers.network.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject

interface RedditApi {
    @GET("/r/{subreddit}.json")
    suspend fun getPosts(@Path("subreddit") subreddit: String): Response<List<Post>>
}

class RedditService @Inject constructor(private val api: RedditApi) : BaseService() {
    suspend fun getPosts(subreddit: String): Result<List<Post>> {
        return apiCall { api.getPosts(subreddit) }
    }
}
