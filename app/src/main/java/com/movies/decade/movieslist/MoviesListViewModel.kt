package com.movies.decade.movieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.movies.decade.businesslogic.MoviesManager
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.statemodels.MoviesUiState
import com.movies.decade.statemodels.MoviesViewModelState
import com.movies.decade.utils.isDeviceOnline
import com.movies.decade.utils.toAdapterList

class MoviesListViewModel(private var moviesManager: MoviesManager) : ViewModel() {

    private var moviesList: List<AdapterItem<Movie>>? = null

    val viewState: MutableLiveData<MoviesUiState> =
        Transformations.switchMap(moviesManager.viewModelState) {
            moviesList = toAdapterList(it.movies)

            val newState = MutableLiveData<MoviesUiState>()
            newState.value = MoviesUiState(moviesList, moviesList?.isNotEmpty() == true)

            newState
        } as MutableLiveData<MoviesUiState>

    init {
        searchMovies("")
    }

    fun searchMovies(query: String) {
        if (query.isEmpty() && moviesList == null) {
            moviesManager.search(MoviesViewModelState(query, null, isDeviceOnline()))
            return
        }

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
        viewState.value = MoviesUiState(movies, movies?.isNotEmpty() == true)
    }
}