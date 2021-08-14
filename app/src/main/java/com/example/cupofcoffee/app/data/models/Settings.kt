package com.example.cupofcoffee.app.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubReddit(
    val name: String
): Parcelable

val DEFAULT_SUB_REDDIT = SubReddit("default-sub-reddit")
val POPULAR_SUB_REDDIT = SubReddit("popular")
val PICS_SUB_REDDIT = SubReddit("pics")
val NEWS_SUB_REDDIT = SubReddit("news")
val MEMES_SUB_REDDIT = SubReddit("memes")

val defaultSubs = listOf(
    POPULAR_SUB_REDDIT, PICS_SUB_REDDIT, NEWS_SUB_REDDIT, MEMES_SUB_REDDIT
)


val defaultSubNames = defaultSubs.map { it.name }
