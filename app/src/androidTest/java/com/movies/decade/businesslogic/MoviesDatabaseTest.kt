package com.movies.decade.businesslogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.utils.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.validateMockitoUsage
import com.nhaarman.mockito_kotlin.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoviesDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var moviesDao: MoviesDao? = null
    private var moviesDb: MoviesDatabase? = null

    @Before
    fun setup() {
        moviesDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java
        ).build()

        moviesDao = moviesDb?.moviesDao()
    }

    @After
    fun tearDown() {
        moviesDb?.close()
        validateMockitoUsage()
    }

    @Test
    fun insertMovie() {
        val movie = createMovie(true)
        val ids = moviesDao?.insertMovie(movie)

        assert(ids?.isNotEmpty() ?: false)
    }

    @Test
    fun insertSameMovie() {
        val movie = createMovie(true)
        val previousSize = moviesDao?.getQueriedMovies(movie.title)?.value?.size

        moviesDao?.insertMovie(movie)
        assert(moviesDao?.getQueriedMovies(movie.title)?.value?.size == previousSize?.plus(1))

        moviesDao?.insertMovie(movie)
        assert(moviesDao?.getQueriedMovies(movie.title)?.value?.size == previousSize)
    }

    @Test
    fun getAllMoviesDescendingByYearAndRating() {
        moviesDao?.insertMovie(*getMovieList(5).toTypedArray())

        val queriedMovies = moviesDao?.getAllMovies()

        val observer: Observer<List<Movie>> = mock()
        queriedMovies?.observeForever(observer)

        verify(observer).onChanged(queriedMovies?.value)

        if (queriedMovies?.value != null) {
            val sortedMovies = queriedMovies.value?.let { sortMovies(it).value } ?: emptyList()

            queriedMovies.value?.forEachIndexed { index, movie ->
                assert(movie == sortedMovies[index])
            }
        }
    }

    @Test
    fun getQueriedMovies() {
        val query = "the"

        moviesDao?.insertMovie(*getMovieList(5).toTypedArray())

        moviesDao?.getQueriedMovies(query)?.value?.forEach { assert(it.title.contains(query)) }
    }

    @Test
    fun checkQueriedMoviesSorted() {
        val query = "da"

        moviesDao?.insertMovie(*getMovieList(20).toTypedArray())

        val observer: Observer<List<Movie>> = mock()

        val queriedMovies = moviesDao?.getQueriedMovies(query)
        queriedMovies?.observeForever(observer)

        verify(observer).onChanged(queriedMovies?.value)

        if (queriedMovies?.value != null) {
            val sortedMovies = queriedMovies.value?.let { sortMovies(it).value } ?: emptyList()

            queriedMovies.value?.forEachIndexed { index, movie ->
                assert(movie == sortedMovies[index])
            }
        }
    }
}