package com.movies.decade.businesslogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.movies.decade.businesslogic.models.DbMovie
import com.movies.decade.utils.createMovie
import com.movies.decade.utils.getMovieList
import com.movies.decade.utils.sortMoviesByRating
import com.movies.decade.utils.sortMoviesByYear
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

    private val observer: Observer<List<DbMovie>> = mock()

    @Before
    fun setup() {
        moviesDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java
        ).build()

        moviesDao = moviesDb?.moviesDao()

        moviesDao?.getAllMovies()?.observeForever(observer)
    }

    @After
    fun tearDown() {
        moviesDao?.removeAllMovies()
        moviesDb?.close()
        validateMockitoUsage()
    }

    @Test
    fun insertMovie() {
        val movie = createMovie()
        val ids = moviesDao?.insertMovie(movie)

        assert(ids?.isNotEmpty() ?: false)
    }

    @Test
    fun insertSameMovie() {
        val movie = createMovie()
        val previousSize = moviesDao?.getQueriedMovies(movie.title)?.value?.size

        moviesDao?.insertMovie(movie)
        assert(moviesDao?.getQueriedMovies(movie.title)?.value?.size == previousSize?.plus(1))

        moviesDao?.insertMovie(movie)
        assert(moviesDao?.getQueriedMovies(movie.title)?.value?.size == previousSize)
    }

    @Test
    fun getAllMoviesAscendingByYearAndRating() {
        moviesDao?.insertMovie(*getMovieList(5).toTypedArray())

        verify(observer).onChanged(
            sortMoviesByYear(
                sortMoviesByRating(
                    moviesDao?.getAllMovies()?.value ?: emptyList()
                )
            )
        )
    }

    @Test
    fun getQueriedMovie() {
        val query = "the"
        val previousSize = 5

        moviesDao?.insertMovie(*getMovieList(previousSize).toTypedArray())

        moviesDao?.getQueriedMovies(query)?.value?.forEach { assert(it.title.contains(query)) }
    }

    @Test
    fun deleteAllMovies() {
        moviesDao?.removeAllMovies()
        verify(observer).onChanged(emptyList())
    }
}