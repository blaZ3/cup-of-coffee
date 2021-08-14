package com.example.cupofcoffee.app.data.models

import android.os.Parcelable
import com.example.cupofcoffee.app.data.models.ObjectKind.CommentRef
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import se.ansman.kotshi.JsonDefaultValue
import se.ansman.kotshi.JsonSerializable
import se.ansman.kotshi.Polymorphic
import se.ansman.kotshi.PolymorphicLabel


@JsonSerializable
enum class ResultType {
    Listing
}

@JsonSerializable
enum class ObjectKind {
    @Json(name = "t1")
    Comment,

    @Json(name = "t2")
    Account,

    @Json(name = "t3")
    Link,

    @Json(name = "t4")
    Message,

    @Json(name = "t5")
    Subreddit,

    @Json(name = "t6")
    Award,

    @Transient
    CommentRef
}

@Parcelize
@JsonSerializable
data class ApiResult(
    @Json(name = "kind")
    val resultType: ResultType?,
    val data: Data?
) : Parcelable

@Parcelize
@JsonSerializable
data class Data(
    val modhash: String? = null,
    val dist: Int? = null,
    val after: String? = null,
    val before: String? = null,
    val geo_filter: String? = null,
    val children: List<DataChild?>?,
) : Parcelable


@JsonSerializable
@Polymorphic(labelKey = "kind")
sealed class DataChild(@Json(name = "kind") val kind: ObjectKind?) : Parcelable {
    @JsonSerializable
    @PolymorphicLabel(value = "t3")
    @Parcelize
    data class PostData(
        val data: Post?
    ) : DataChild(ObjectKind.Link), Parcelable

    @JsonSerializable
    @PolymorphicLabel(value = "t1")
    @Parcelize
    data class CommentData(
        val data: Comment?
    ) : DataChild(ObjectKind.Comment), Parcelable

    @JsonSerializable
    @JsonDefaultValue
    @PolymorphicLabel(value = "")
    @Parcelize
    data class CommentReference(val id: String?) : DataChild(CommentRef), Parcelable
}
