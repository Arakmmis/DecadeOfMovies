package com.movies.decade.businesslogic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.statemodels.MoviesViewModelState
import com.movies.decade.utils.getFlickrImageUrl
import com.movies.decade.utils.sortMovies
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesManager(var moviesDao: MoviesDao, var moviesRepository: MoviesRepository) {

    var viewModelState = MutableLiveData<MoviesViewModelState>()

    fun search(state: MoviesViewModelState) {
        var movies: List<Movie>? = moviesDao.getQueriedMovies(state.query ?: "").value

        movies = getMoviesOrPopulate(movies, state.loadImages ?: false)

        viewModelState.value = MoviesViewModelState(state.query, movies, state.loadImages)
    }

    private fun getMoviesOrPopulate(movies: List<Movie>?, loadImage: Boolean): List<Movie> {
        return if (movies == null) {
            var sortedMovies = getSortedMoviesFromFile()

            if (loadImage)
                sortedMovies = sortedMovies.map { retrieveMovieImages(it) }

            populateDb(sortedMovies)

            sortedMovies
        } else
            movies
    }

    private fun getSortedMoviesFromFile(): List<Movie> {
        val unSortedMovies = moviesRepository.getMoviesFromFile().value
        return sortMovies(unSortedMovies ?: emptyList())
    }

    private fun populateDb(movies: List<Movie>) {
        Single.create<Unit> { moviesDao.insertMovie(*movies.toTypedArray()) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun retrieveMovieImages(movie: Movie): Movie {
        val disposable = CompositeDisposable()

        disposable.add(moviesRepository.getMovieImageList(movie)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { images, e ->
                if (e != null) {
                    Log.e("MM", e.localizedMessage ?: "")
                    disposable.clear()
                    return@subscribe
                }

                val imageUrlList = ArrayList<String>()

                images?.wrapper?.photos?.forEach { photo ->
                    val url = getFlickrImageUrl(photo)
                    imageUrlList.add(url)
                }

                movie.imagesUrls = imageUrlList

                disposable.clear()
            }
        )

        return movie
    }
}