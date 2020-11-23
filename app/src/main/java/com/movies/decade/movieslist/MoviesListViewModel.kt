package com.movies.decade.movieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.movies.decade.businesslogic.MoviesManager
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.uimodels.MoviesUiModel
import com.movies.decade.utils.toAdapterList
import org.koin.java.KoinJavaComponent.inject

class MoviesListViewModel : ViewModel() {

    private val moviesManager: MoviesManager by inject(MoviesManager::class.java)

    private var moviesList: List<AdapterItem<Movie>>? = null

    val viewState: MutableLiveData<MoviesUiModel> =
        Transformations.switchMap(moviesManager.queriedMovies) { movies ->
            moviesList = toAdapterList(movies)

            val newState = MutableLiveData<MoviesUiModel>()
            newState.value = MoviesUiModel(moviesList, moviesList?.isNotEmpty() == true)

            newState
        } as MutableLiveData<MoviesUiModel>

    init {
        searchMovies("")
    }

    fun searchMovies(query: String) {
        if (query.isEmpty())
            moviesManager.query.value = query

        pushState(filterSearchByQuery(query))
    }

    private fun filterSearchByQuery(query: String): List<AdapterItem<Movie>>? {
        val queriedMovies = moviesList?.filter { item ->
            item.value.title.toLowerCase().contains(query.toLowerCase())
                    || item.value.year.toString().toLowerCase().contains(query.toLowerCase())
                    || isQueryInList(query, item.value.genres)
                    || isQueryInList(query, item.value.cast)
        }

        return if (queriedMovies.isNullOrEmpty())
            null
        else
            queriedMovies
    }

    private fun isQueryInList(query: String, list: List<String>?): Boolean {
        return list?.any { it.toLowerCase().contains(query) } ?: false
    }

    private fun pushState(movies: List<AdapterItem<Movie>>?) {
        viewState.value = MoviesUiModel(movies, movies?.isNotEmpty() == true)
    }
}