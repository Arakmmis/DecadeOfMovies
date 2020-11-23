package com.movies.decade.statemodels

import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie

data class MoviesUiState(val movies: List<AdapterItem<Movie>>?, val hasList: Boolean)