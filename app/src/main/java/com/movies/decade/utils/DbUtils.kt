package com.movies.decade.utils

import com.movies.decade.businesslogic.models.DbMovie
import java.util.*
import kotlin.collections.set
import kotlin.random.Random

val dummyMovieTitles = listOf("(500) Days of Summer", "12 Rounds", "17 Again", "2012", "9")

val dummyMovieCast = listOf(
    "Zac Efron",
    "Leslie Mann",
    "Thomas Lennon",
    "Matthew Perry",
    "Melora Hardin",
    "Michelle Trachtenberg",
    "Sterling Knight"
)

val dummyMovieGenres = listOf(
    "Comedy",
    "Teen",
    "Drama",
    "Animated",
    "Horror",
    "Science Fiction"
)

val dummyMovieImages = listOf(
    "https://images-na.ssl-images-amazon.com/images/I/91WNnQZdybL._AC_SL1500_.jpg",
    "https://images-na.ssl-images-amazon.com/images/I/51LNtFacJBL._AC_.jpg",
    "https://images-na.ssl-images-amazon.com/images/I/71nsvxFpSTL._AC_SL1200_.jpg",
    "https://www.reeldeals.com/Images/HomeImages/36692.jpg",
    "https://cdn.shopify.com/s/files/1/0185/4636/products/IRONGIANT_POSTER_upd_ee164942-12aa-4f96-a339-8cd745e83b1e_800x.jpg?v=1571606999"
)

fun createMovie(): DbMovie {
    return DbMovie(
        Random.nextInt(1, 100),
        dummyMovieTitles.shuffled().first(),
        Random.nextInt(1990, 2020),
        dummyMovieCast.shuffled().subList(0, Random.nextInt(1, 5)),
        dummyMovieGenres.shuffled().subList(0, Random.nextInt(1, 5)),
        Random.nextInt(1, 10),
        dummyMovieImages.shuffled().subList(0, Random.nextInt(1, 4))
    )
}

fun getMovieList(size: Int): List<DbMovie> {
    val movieList = ArrayList<DbMovie>()

    for (i in 0 until size)
        movieList.add(createMovie())

    return movieList
}

fun sortMoviesByYear(movies: List<DbMovie>): List<DbMovie> {
    val map = getMoviesByYear(movies)
    val list = mutableListOf<DbMovie>()
    map.forEach { entry: Map.Entry<Int, ArrayList<DbMovie>> ->
        entry.value.forEach { movie -> list.add(movie) }
    }

    return list
}

fun getMoviesByYear(movies: List<DbMovie>): Map<Int, ArrayList<DbMovie>> {
    val yearMap = TreeMap<Int, ArrayList<DbMovie>>()

    movies.forEach { dbMovie ->
        val list: ArrayList<DbMovie> =
            yearMap[dbMovie.year] ?: ArrayList<DbMovie>()

        list.add(dbMovie)

        yearMap[dbMovie.year] = list
    }

    return yearMap
}

fun sortMoviesByRating(movies: List<DbMovie>): List<DbMovie> {
    if (movies.isEmpty() || movies.size == 1) return movies

    val middle = movies.size / 2
    val left = movies.subList(0, middle)
    val right = movies.subList(middle, movies.size)

    return merge(sortMoviesByRating(left), sortMoviesByRating(right))
}

private fun merge(left: List<DbMovie>, right: List<DbMovie>): List<DbMovie> {
    var indexLeft = 0
    var indexRight = 0
    val newList: MutableList<DbMovie> = mutableListOf()

    while (indexLeft < left.size && indexRight < right.size) {
        if (left[indexLeft].compareByRating(right[indexRight]) == -1) {
            newList.add(left[indexLeft])
            indexLeft++
        } else {
            newList.add(right[indexRight])
            indexRight++
        }
    }

    while (indexLeft < left.size) {
        newList.add(left[indexLeft])
        indexLeft++
    }

    while (indexRight < right.size) {
        newList.add(right[indexRight])
        indexRight++
    }

    return newList
}
