package com.movies.decade.businesslogic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RetrievedMovies(
    @SerializedName("movies")
    @Expose
    val movies: List<Movie>
)