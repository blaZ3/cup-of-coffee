package com.example.cupofcoffee.app.data.models

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Post(
    val title: String? = null,
    val name: String? = null,
    val subreddit: String? = null,
    @Json(name = "author_fullname")
    val authorFullName: String? = null,
    val author: String? = null,
    @Json(name = "is_original_content")
    val isOriginalContent: Boolean = false,
    @Json(name = "over_18")
    val over18: Boolean = false,
    val spoiler: Boolean = false,
    val created: Long = -1,
    val preview: Preview? = null,
    @Json(name = "upvote_ratio")
    val upvoteRatio: Float = -1f,
    val ups: Long = -1,
    @Json(name = "total_awards_received")
    val totalAwardsReceived: Int = -1,
    @Json(name = "is_video")
    val isVideo: Boolean = false,

    val cleanedImageUrl: String? = null
) {
    val isText: Boolean get() = !isVideo && preview == null
    val isImage: Boolean get() = !isVideo && preview != null
    val postName: String? get() = name?.substring(0, name.indexOfFirst { it == '_' })
}

@JsonSerializable
data class Comment(
    @Json(name = "total_awards_received")
    val totalAwardsReceived: Int?,
    @Json(name = "link_id")
    val linkId: String?,
    val body: String
)
