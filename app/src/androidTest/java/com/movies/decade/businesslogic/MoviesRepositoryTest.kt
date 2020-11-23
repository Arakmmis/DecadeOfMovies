package com.movies.decade.businesslogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.movies.decade.utils.MOVIES_FILE_NAME
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.inject

@RunWith(AndroidJUnit4::class)
class MoviesRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val moviesRepository: MoviesRepository by inject(MoviesRepository::class.java)

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun checkGetMoviesReturnNonEmptyList() {
        assert(moviesRepository.getMoviesFromFile(MOVIES_FILE_NAME).value?.isNotEmpty() ?: false)
    }
}