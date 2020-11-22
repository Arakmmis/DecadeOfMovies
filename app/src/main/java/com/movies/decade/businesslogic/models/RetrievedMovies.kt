package com.movies.decade.businesslogic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RetrievedMovies(
    @SerializedName("movies")
    @Expose
    val movies: List<Movie>
) {

    data class Movie(
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("year")
        @Expose
        val year: Int,
        @SerializedName("cast")
        @Expose
        val cast: List<String>? = null,
        @SerializedName("genres")
        @Expose
        val genres: List<String>,
        @SerializedName("rating")
        @Expose
        val rating: Int
    )
}