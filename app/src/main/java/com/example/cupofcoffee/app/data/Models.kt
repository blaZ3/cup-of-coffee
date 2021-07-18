package com.example.cupofcoffee.app.data

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable


@JsonSerializable
enum class ResultKind {
    Listing
}

@JsonSerializable
data class PostResult(
    val kind: ResultKind? = null,
    val data: PostResultData? = null
)

@JsonSerializable
data class PostResultData(
    val modhash: String? = null,
    val after: String? = null,
    val children: List<PostInfo>? = null
)

@JsonSerializable
data class PostInfo(
    val kind: String? = null,
    @Json(name = "data")
    val post: Post? = null
)

@JsonSerializable
data class Post(
    val title: String? = null,
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
) {
    val isText: Boolean get() = !isVideo && preview == null
    val isImage: Boolean get() = !isVideo && preview != null
}

@JsonSerializable
data class Preview(
    val images: List<PreviewImage>? = null
)

@JsonSerializable
data class PreviewImage(
    val source: ImageSource? = null,
    val resolutions: List<ImageSource>? = null
)

@JsonSerializable
data class ImageSource(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null
) {
    fun getCleanedUrl(): String? {
        return url?.let {
            if (it.contains("&amp;")) return it.replace("&amp;", "&")
            else it
        }
    }
}
