package com.movies.decade.businesslogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.inject

@RunWith(AndroidJUnit4::class)
class MoviesRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val moviesRepository: MoviesRepository by inject(MoviesRepository::class.java)

    @Test
    fun checkGetMoviesReturnNonEmptyList() {
        assert(moviesRepository.getMoviesFromFile().value?.isNotEmpty() ?: false)
    }
}