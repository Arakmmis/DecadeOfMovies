package com.movies.decade.businesslogic

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.statemodels.MoviesViewModelState
import com.movies.decade.utils.getFlickrImageUrl
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesManager(var moviesDao: MoviesDao, var moviesRepository: MoviesRepository) {

    private val moviesMediator = MediatorLiveData<List<Movie>>()

    val viewModelState: MutableLiveData<MoviesViewModelState> =
        Transformations.switchMap(moviesMediator) {
            val newState = MutableLiveData<MoviesViewModelState>()

            newState.value = MoviesViewModelState(it, null)
            newState
        } as MutableLiveData<MoviesViewModelState>

    fun getMovies(state: MoviesViewModelState) {
        viewModelState.value = MoviesViewModelState(state.movies, state.loadImages)
        subscribeToMovies()
    }

    private fun subscribeToMovies() {
        moviesMediator.addSource(getMoviesFromDb()) {
            if (!it.isNullOrEmpty())
                viewModelState.value =
                    MoviesViewModelState(it, viewModelState.value?.loadImages)
            else
                getMoviesFromFile()
        }
    }

    private fun getMoviesFromDb(): MutableLiveData<List<Movie>?> {
        return Transformations.switchMap(moviesDao.getAllMovies()) {
            val newState = MutableLiveData<List<Movie>>()
            newState.value = it

            newState
        } as MutableLiveData<List<Movie>?>
    }

    private fun getMoviesFromFile() {
        moviesMediator.addSource(moviesRepository.getMoviesFromFile()) {
            if (it.isNullOrEmpty()) {
                viewModelState.value =
                    MoviesViewModelState(emptyList(), viewModelState.value?.loadImages)
            } else {
                val movies = if (viewModelState.value?.loadImages == true)
                    getMoviesWithImages(it)
                else
                    it

                populateDb(movies)
            }
        }
    }

    private fun getMoviesWithImages(movies: List<Movie>?): List<Movie> {
        if (movies.isNullOrEmpty()) return emptyList()

        return movies.map { retrieveMovieImages(it) }
    }

    private fun retrieveMovieImages(movie: Movie): Movie {
        val disposable = CompositeDisposable()

        disposable.add(moviesRepository.getMovieImageList(movie)
            .subscribeOn(Schedulers.io())
            .subscribe { images, e ->
                if (e != null) {
                    Log.e("MM", e.localizedMessage)
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
            })

        return movie
    }

    private fun populateDb(movies: List<Movie>) {
        if (movies.isEmpty()) return

        val disposable = CompositeDisposable()

        disposable.add(Single.create<List<Long>> {
            moviesDao.insertMovie(*movies.toTypedArray()).isNotEmpty()
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { ids, e ->
                if (e != null) {
                    e.printStackTrace()
                    disposable.clear()
                    return@subscribe
                }

                if (ids.isNotEmpty()) {
                    getMoviesFromDb()
                    disposable.clear()
                } else {
                    disposable.clear()
                    throw Exception("Failure to insert movies in Database")
                }
            })
    }
}