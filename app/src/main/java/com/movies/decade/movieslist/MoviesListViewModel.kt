package com.movies.decade.movieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.movies.decade.businesslogic.MoviesManager
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.statemodels.MoviesUiState
import com.movies.decade.statemodels.MoviesViewModelState
import com.movies.decade.utils.isDeviceOnline
import com.movies.decade.utils.sortMovies
import com.movies.decade.utils.toAdapterList

class MoviesListViewModel(private var moviesManager: MoviesManager) : ViewModel() {

    private var moviesList: List<Movie>? = null

    val viewState: MutableLiveData<MoviesUiState> =
        Transformations.switchMap(moviesManager.viewModelState) {
            moviesList = it.movies

            val newState = MutableLiveData<MoviesUiState>()
            newState.value =
                MoviesUiState(toAdapterList(it.movies), it.movies == null)

            newState
        } as MutableLiveData<MoviesUiState>

    init {
        searchMovies("")
    }

    fun searchMovies(query: String) {
        if (query.isEmpty() && moviesList == null) {
            moviesManager.getMovies(MoviesViewModelState(null, isDeviceOnline()))
            return
        }

        pushState(sortMovies(filterSearchByQuery(query) ?: emptyList(), true).value)
    }

    private fun filterSearchByQuery(query: String): List<Movie>? {
        val queriedMovies = moviesList?.filter { item ->
            item.title.toLowerCase().contains(query.toLowerCase())
                    || item.year.toString().toLowerCase().contains(query.toLowerCase())
                    || isQueryInList(query, item.genres)
                    || isQueryInList(query, item.cast)
        }

        return if (queriedMovies.isNullOrEmpty())
            null
        else
            queriedMovies
    }

    private fun isQueryInList(query: String, list: List<String>?): Boolean {
        return list?.any { it.toLowerCase().contains(query) } ?: false
    }

    private fun pushState(movies: List<Movie>?) {
        viewState.value = MoviesUiState(toAdapterList(movies), movies?.isNotEmpty() == true)
    }
}