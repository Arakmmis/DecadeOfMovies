package com.movies.decade.businesslogic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("photos")
    @Expose
    val wrapper: Images
) {
    data class Images(
        @SerializedName("photo")
        @Expose
        val photos: List<ImageObject>
    ) {
        data class ImageObject(
            @SerializedName("id")
            @Expose
            val id: String,
            @SerializedName("secret")
            @Expose
            val secret: String,
            @SerializedName("server")
            @Expose
            val server: String,
            @SerializedName("farm")
            @Expose
            val farm: Int
        )
    }
}