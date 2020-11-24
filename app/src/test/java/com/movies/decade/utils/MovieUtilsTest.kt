package com.movies.decade.utils

import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie
import org.junit.Test

class MovieUtilsTest {

    @Test
    fun `assert movies list size for a specific size sent`() {
        val movies = getMovieList(5)
        assert(movies.size == 5)
    }

    @Test
    fun `assert movies map is sorted according to year`() {
        val movies = getMovieList(10)
        val yearsSet = getDiffYears(movies)

        val sortedMap = sortMoviesByYear(movies)

        assert(sortedMap.size == yearsSet.size)

        sortedMap.entries.forEachIndexed { index, entry ->
            assert(entry.key == yearsSet.elementAt(index))
        }
    }

    @Test
    fun `assert movies list contains all years after sorting by year`() {
        val movies = getMovieList(7)
        val yearsSet = getDiffYears(movies)

        sortMoviesByYear(movies).forEach {
            assert(yearsSet.contains(it.key))
        }
    }

    @Test
    fun `assert movies list contains all movies after separation by years`() {
        val movies = getMovieList(6)
        val moviesSortedByYear = sortMoviesByYear(movies)

        assert(moviesSortedByYear.size == movies.size)
    }

    @Test
    fun `assert movies list is sorted correctly by rating`() {
        val movies = getMovieList(10)
        val moviesSortedByRating = sortMoviesByRating(movies).asReversed()

        assert(checkRatingOrder(moviesSortedByRating))
    }

    @Test
    fun `assert movies list is trimmed to 5 movies per year`() {
        val movies = getMovieList(20)
        val sortedMap = sortMoviesByYear(movies)

        sortedMap.entries.forEach { entry ->
            assert(entry.value.size <= MAX_MOVIES_PER_YEAR)
        }
    }

    @Test
    fun `assert adapter list is greater than movie list`() {
        val initialMoviesSize = 10
        val adapterList = toAdapterList(getMovieList(initialMoviesSize))
        assert(adapterList?.size?.let { it > initialMoviesSize } ?: false)
    }

    @Test
    fun `assert adapter list is list of movies separated by years`() {
        val initialMoviesSize = 10
        val adapterList = toAdapterList(getMovieList(initialMoviesSize))

        var currentItemType = adapterList?.get(0)?.viewType

        adapterList?.forEach { item ->
            if (currentItemType == AdapterItem.TYPE_YEAR) {
                if (item.viewType == AdapterItem.TYPE_YEAR) {
                    currentItemType = -1
                    return
                } else {
                    currentItemType = item.viewType
                }
            } else {
                currentItemType = item.viewType
            }
        }

        assert(currentItemType != -1)
    }

    private fun getDiffYears(movies: List<Movie>): Set<Int> {
        val years = HashSet<Int>()
        movies.forEach { years.add(it.year) }

        return years.toSortedSet()
    }

    private fun checkRatingOrder(movies: List<Movie>): Boolean {
        movies.forEachIndexed { index, movie ->
            if (index != 0 && index != movies.size - 1) {
                if (movie.rating > movies[index - 1].rating
                    || movie.rating < movies[index + 1].rating
                )
                    return false
            }
        }

        return true
    }
}