package com.movies.decade.movieslist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movies.decade.businesslogic.MoviesDao
import com.movies.decade.businesslogic.MoviesDatabase
import com.movies.decade.businesslogic.MoviesRepository
import com.movies.decade.uimodels.MoviesUiModel
import org.koin.java.KoinJavaComponent.inject

class MoviesListViewModel(context: Context) : ViewModel() {

    val viewState: MutableLiveData<MoviesUiModel> = MutableLiveData()

    val moviesRepository: MoviesRepository by inject(MoviesRepository::class.java)
    val moviesDao: MoviesDao by lazy {
        MoviesDatabase.getDatabase(context).moviesDao()
    }

    init {
        val uiModel = MoviesUiModel(emptyList(), false)
        viewState.value = uiModel

        searchMovies("")
    }

    fun searchMovies(query: String) {

    }
}