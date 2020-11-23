package com.movies.decade.utils

import com.movies.decade.businesslogic.models.ImagesResponse

const val MOVIES_FILE_NAME = "movies.json"

const val FLICKR_KEY = "959a1549600658b3c797bfbde64ae52c"

const val FLICKR_BASE_URL = "https://api.flickr.com/services/rest/"
const val FLICKR_IMAGE_API =
    "?method=flickr.photos.search&api_key=$FLICKR_KEY&format=json&per_page=10&nojsoncallback=1"

fun getFlickrImageUrl(photo: ImagesResponse.Images.ImageObject): String {
    val builder = StringBuilder()

    builder.append("https://farm​")
        .append(photo.farm)
        .append(".static.flickr.com/")
        .append(photo.server)
        .append("/")
        .append(photo.id)
        .append("_")
        .append(photo.secret)
        .append(".jpg")

    return builder.toString()
}
