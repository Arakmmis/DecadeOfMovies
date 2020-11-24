package com.movies.decade.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie
import java.util.*
import kotlin.collections.ArrayList
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

const val MAX_MOVIES_PER_YEAR = 5

fun createMovie(withImages: Boolean): Movie {
    return Movie(
        dummyMovieTitles.shuffled().first(),
        Random.nextInt(1990, 2020),
        dummyMovieCast.shuffled().subList(0, Random.nextInt(1, 5)),
        dummyMovieGenres.shuffled().subList(0, Random.nextInt(1, 5)),
        Random.nextInt(1, 10),
        if (withImages) dummyMovieImages.shuffled().subList(0, Random.nextInt(1, 4)) else null
    )
}

fun getMovieList(size: Int): List<Movie> {
    val movieList = ArrayList<Movie>()

    for (i in 0 until size)
        movieList.add(createMovie(true))

    return movieList
}

fun sortMovies(movies: List<Movie>, capResults: Boolean = false): LiveData<List<Movie>> {
    val observable = MutableLiveData<List<Movie>>()

    if (movies.isEmpty()) {
        observable.value = emptyList()
        return observable
    }

    observable.value = sortMoviesByYear(movies, capResults)
        .mapValues { sortMoviesByRating(it.value) }
        .flatMap { it.value }
        .asReversed()

    return observable
}

fun sortMoviesByRating(movies: List<Movie>): List<Movie> {
    if (movies.isEmpty() || movies.size == 1) return movies

    val middle = movies.size / 2
    val left = movies.subList(0, middle)
    val right = movies.subList(middle, movies.size)

    return merge(sortMoviesByRating(left), sortMoviesByRating(right))
}

private fun merge(left: List<Movie>, right: List<Movie>): List<Movie> {
    var indexLeft = 0
    var indexRight = 0
    val newList: MutableList<Movie> = mutableListOf()

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

fun sortMoviesByYear(movies: List<Movie>, capResults: Boolean = false): Map<Int, List<Movie>> {
    val yearMap = TreeMap<Int, ArrayList<Movie>>()

    movies.forEach { movie ->
        val list: ArrayList<Movie> =
            yearMap[movie.year] ?: ArrayList()

        if (capResults) {
            if (list.size < MAX_MOVIES_PER_YEAR)
                list.add(movie)
        } else {
            list.add(movie)
        }

        yearMap[movie.year] = list
    }

    return yearMap
}

fun toAdapterList(movies: List<Movie>?): List<AdapterItem<Movie>>? {
    if (movies.isNullOrEmpty()) return null

    val adapterList = ArrayList<AdapterItem<Movie>>()

    var previousYear = 0

    movies.forEach { movie ->
        if (previousYear != movie.year) {
            val dummyMovie = Movie("", movie.year, null, null, 0, null)

            val item = AdapterItem(dummyMovie, AdapterItem.TYPE_YEAR)
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