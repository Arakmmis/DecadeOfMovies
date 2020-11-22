package com.movies.decade.businesslogic.models

data class DbMovie(
    val title: String,
    val year: Int,
    val cast: List<String>? = null,
    val genres: List<String>,
    val rating: Int,
    val imagesUrls: List<String>? = null
) : Movie