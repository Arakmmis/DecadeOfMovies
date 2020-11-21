package com.movies.decade.movieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movies.decade.businesslogic.MoviesDao
import com.movies.decade.businesslogic.MoviesRepository
import com.movies.decade.uimodels.MoviesUiModel
import org.koin.java.KoinJavaComponent.inject

class MoviesListViewModel : ViewModel() {

    val viewState : MutableLiveData<MoviesUiModel> = MutableLiveData()

    val moviesRepository : MoviesRepository by inject(MoviesRepository::class.java)
    val moviesDao : MoviesDao by inject(MoviesDao::class.java)

    fun searchMovies(query: String) {

    }
}