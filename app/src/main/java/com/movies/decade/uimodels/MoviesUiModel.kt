package com.movies.decade.uimodels

import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie

data class MoviesUiModel(val movies: List<AdapterItem<Movie>>?, val hasList: Boolean)