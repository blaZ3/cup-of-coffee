package com.example.cupofcoffee.app.data

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable


@JsonSerializable
data class Post(
    val kind: String,
    val data: PostData
)

@JsonSerializable
data class PostData(
    val title: String,
    val subreddit: String,
    @Json(name = "author_fullname")
    val authorFullName: String,
    val author: String,
    @Json(name = "is_original_content")
    val isOriginalContent: Boolean,
    @Json(name = "over_18")
    val over18: Boolean,
    val spoiler: Boolean,
    val created: Long,
    val preview: Preview? = null,
    @Json(name = "upvote_ratio")
    val upvoteRatio: Float,
    val ups: Long,
    @Json(name = "total_awards_received")
    val totalAwardsReceived: Int,
    @Json(name = "is_video")
    val isVideo: Boolean,
) {
    val isText: Boolean get() = !isVideo && preview == null
    val isImage: Boolean get() = !isVideo && preview != null
}

@JsonSerializable
data class Preview(
    val images: List<PreviewImage>
)

@JsonSerializable
data class PreviewImage(
    val source: ImageSource,
    val resolutions: List<ImageSource>
)

@JsonSerializable
data class ImageSource(
    val url: String,
    val width: Int,
    val height: Int
)
