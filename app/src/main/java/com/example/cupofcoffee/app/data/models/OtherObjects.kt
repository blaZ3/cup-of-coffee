package com.example.cupofcoffee.app.data.models

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Preview(
    val images: List<PreviewImage>? = null
)

@JsonSerializable
data class PreviewImage(
    val source: ImageSource? = null,
    val resolutions: List<ImageSource>? = null
) {
    val small = resolutions?.first()
    val high = source
}

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
