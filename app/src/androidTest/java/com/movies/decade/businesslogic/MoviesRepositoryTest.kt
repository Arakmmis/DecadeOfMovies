package com.movies.decade.businesslogic

import org.junit.After
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.inject

class MoviesRepositoryTest {

    private val moviesRepository: MoviesRepository by inject(MoviesRepository::class.java)

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun checkGetMoviesReturnNonEmptyList() {
        assert(moviesRepository.getMovies().isNotEmpty())
    }
}