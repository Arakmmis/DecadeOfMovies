package com.movies.decade.utils

import com.movies.decade.businesslogic.models.DbMovie
import org.junit.Test

class DbUtilsTest {

    @Test
    fun `assert movies list size for a specific size sent`() {
        val movies = getMovieList(5)
        assert(movies.size == 5)
    }

    @Test
    fun `assert movies map is sorted according to year`() {
        val movies = getMovieList(10)
        val yearsSet = getDiffYears(movies)

        val sortedMap = getMoviesByYear(movies)

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
            assert(yearsSet.contains(it.year))
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
        val moviesSortedByRating = sortMoviesByRating(movies)

        assert(checkRatingOrder(moviesSortedByRating))
    }

    private fun getDiffYears(movies: List<DbMovie>): Set<Int> {
        val years = HashSet<Int>()
        movies.forEach { movies -> years.add(movies.year) }

        return years.toSortedSet()
    }

    private fun checkRatingOrder(movies: List<DbMovie>): Boolean {
        movies.forEachIndexed { index, movie ->
            if (index != 0 && index != movies.size - 1) {
                if (movie.rating < movies[index - 1].rating
                    || movie.rating > movies[index + 1].rating
                )
                    return false
            }
        }

        return true
    }
}