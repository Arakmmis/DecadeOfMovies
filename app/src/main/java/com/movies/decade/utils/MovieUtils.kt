package com.movies.decade.utils

import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie

fun toAdapterList(movies: List<Movie>): List<AdapterItem<Movie>> {
    if (movies.isEmpty()) return emptyList()

    val adapterList = ArrayList<AdapterItem<Movie>>()

    var previousYear = 0

    movies.forEach { movie ->
        if (previousYear != movie.year) {
            val item = AdapterItem(movie, AdapterItem.TYPE_YEAR)
            adapterList.add(item)

            val movieItem = AdapterItem(movie, AdapterItem.TYPE_MOVIE)
            adapterList.add(movieItem)

            previousYear = movie.year
        } else {
            val movieItem = AdapterItem(movie, AdapterItem.TYPE_MOVIE)
            adapterList.add(movieItem)
        }
    }

    return adapterList
}