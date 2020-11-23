package com.movies.decade.businesslogic

import androidx.lifecycle.LiveData
import com.movies.decade.businesslogic.models.ImagesResponse
import com.movies.decade.utils.FLICKR_IMAGE_API
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlickrApiService {

    @GET(FLICKR_IMAGE_API)
    fun getMoviesImage(@Query("text") query: String): Single<ImagesResponse>
}