package com.example.cupofcoffee.app.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
@Parcelize
data class Post(
    val title: String? = null,
    @Json(name = "name")
    val postFullName: String? = null,
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
    val ups: Long = 0,
    val downs: Long = 0,
    val score: Long = 0,
    @Json(name = "total_awards_received")
    val totalAwardsReceived: Int = -1,
    @Json(name = "is_video")
    val isVideo: Boolean = false,


    ) : Parcelable {
    val isText: Boolean get() = !isVideo && preview == null
    val isImage: Boolean get() = !isVideo && preview != null

    @IgnoredOnParcel
    val createdAtStr = created.toString()

    @IgnoredOnParcel
    val cleanedImageUrl: String? = preview?.images?.first()?.source?.getCleanedUrl()
}


@JsonSerializable
data class Comment(
    @Json(name = "total_awards_received")
    val totalAwardsReceived: Int? = null,
    val body: String? = null,
    @Json(name = "body_html")
    val bodyHtml: String? = null,
    @Json(name = "link_id")
    val linkId: String? = null,
    @Json(name = "replies")
    val repliesResult: ApiResult? = null,
    @Json(name = "parent_id")
    val parentId: String? = null,
    @Json(name = "author_fullname")
    val authorFullName: String? = null,
    val author: String? = null,
    val downs: Int = 0,
    val ups: Int = 0,
    val score: Int = 0,
    @Json(name = "subreddit_id")
    val subredditId: String? = null,
    val permalink: String? = null,
    val name: String? = null,
    val created: Long = 0,
    @Json(name = "created_utc")
    val createdUTC: Long = 0,
    val subreddit: String? = null,
) {
    val replies: List<Comment>?
        get() = repliesResult?.data?.children?.mapNotNull {
            if (it is DataChild.CommentData) it.data else null
        }
}


fun String.asShortName(): String {
    return this.substring(
        indexOfFirst { it == '_' } + 1,
        length
    )
}
