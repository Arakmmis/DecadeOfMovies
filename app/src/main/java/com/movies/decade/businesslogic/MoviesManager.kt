package com.movies.decade.businesslogic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.utils.MOVIES_FILE_NAME
import com.movies.decade.utils.sortMovies
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class MoviesManager(context: Context) {

    private val moviesDao by lazy { MoviesDatabase.getDatabase(context).moviesDao() }
    private val moviesRepository by inject(MoviesRepository::class.java)

    var query = MutableLiveData<String>()

    val queriedMovies: MutableLiveData<List<Movie>> = Transformations.switchMap(query) {
        val movies: List<Movie>? = moviesDao.getQueriedMovies(it).value
        getMoviesOrPopulate(movies)
    } as MutableLiveData<List<Movie>>

    private fun getMoviesOrPopulate(movies: List<Movie>?): LiveData<List<Movie>> {
        val mutableData = MutableLiveData<List<Movie>>()

        if (movies == null) {
            val sortedMovies = retrieveMoviesImages(getSortedMoviesFromFile())
            populateDb(sortedMovies)

            mutableData.value = sortedMovies
        } else
            mutableData.value = movies

        return mutableData
    }

    private fun getSortedMoviesFromFile(): List<Movie> {
        val unSortedMovies = moviesRepository.getMoviesFromFile(MOVIES_FILE_NAME).value
        return sortMovies(unSortedMovies ?: emptyList())
    }

    private fun retrieveMoviesImages(movies: List<Movie>) = moviesRepository.getMoviesImages(movies)

    private fun populateDb(movies: List<Movie>) {
        Single.create<Unit> { moviesDao.insertMovie(*movies.toTypedArray()) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}