package com.example.cupofcoffee.app.data.models

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
@Parcelize
data class Preview(
    val images: List<PreviewImage>? = null
) : Parcelable

@JsonSerializable
@Parcelize
data class PreviewImage(
    val source: ImageSource? = null,
    val resolutions: List<ImageSource>? = null
) : Parcelable {
    @IgnoredOnParcel
    val small = resolutions?.first()
    @IgnoredOnParcel
    val high = source
}

@JsonSerializable
@Parcelize
data class ImageSource(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null
) : Parcelable{
    fun getCleanedUrl(): String? {
        return url?.let {
            if (it.contains("&amp;")) return it.replace("&amp;", "&")
            else it
        }
    }
}
