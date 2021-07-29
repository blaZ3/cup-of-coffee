package com.example.cupofcoffee.app.data.models

import com.example.cupofcoffee.app.data.models.ObjectKind.CommentRef
import com.squareup.moshi.Json
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

@JsonSerializable
data class ApiResult(
    @Json(name = "kind")
    val resultType: ResultType?,
    val data: Data?
)

@JsonSerializable
data class Data(
    val modhash: String? = null,
    val dist: Int? = null,
    val after: String? = null,
    val before: String? = null,
    val geo_filter: String? = null,
    val children: List<DataChild?>?,
)

@JsonSerializable
@Polymorphic(labelKey = "kind")
sealed class DataChild(@Json(name = "kind") val kind: ObjectKind?) {
    @JsonSerializable
    @PolymorphicLabel(value = "t3")
    data class PostData(
        val data: Post?
    ) : DataChild(ObjectKind.Link)

    @JsonSerializable
    @PolymorphicLabel(value = "t1")
    data class CommentData(
        val data: Comment?
    ) : DataChild(ObjectKind.Comment)

    @JsonSerializable
    @JsonDefaultValue
    @PolymorphicLabel(value = "")
    data class CommentReference(val id: String?) : DataChild(CommentRef)
}
