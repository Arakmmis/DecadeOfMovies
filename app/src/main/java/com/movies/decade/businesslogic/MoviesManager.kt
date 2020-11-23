package com.movies.decade.businesslogic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.statemodels.MoviesViewModelState
import com.movies.decade.utils.getFlickrImageUrl
import com.movies.decade.utils.sortMovies
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class MoviesManager(context: Context) {

    private val moviesDao by lazy { MoviesDatabase.getDatabase(context).moviesDao() }
    private val moviesRepository by inject(MoviesRepository::class.java)

    var viewModelState = MutableLiveData<MoviesViewModelState>()

    val queriedMovies: MutableLiveData<List<Movie>> = Transformations.switchMap(viewModelState) {
        val movies: List<Movie>? = moviesDao.getQueriedMovies(it.query).value
        getMoviesOrPopulate(movies, it.loadImages)
    } as MutableLiveData<List<Movie>>

    private fun getMoviesOrPopulate(
        movies: List<Movie>?,
        loadImage: Boolean
    ): LiveData<List<Movie>> {
        val mutableData = MutableLiveData<List<Movie>>()

        if (movies == null) {
            var sortedMovies = getSortedMoviesFromFile()

            if (loadImage)
                sortedMovies = sortedMovies.map { retrieveMovieImages(it) }

            populateDb(sortedMovies)

            mutableData.value = sortedMovies
        } else
            mutableData.value = movies

        return mutableData
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