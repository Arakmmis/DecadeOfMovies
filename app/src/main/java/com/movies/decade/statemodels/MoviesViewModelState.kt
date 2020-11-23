package com.movies.decade.statemodels

import com.movies.decade.businesslogic.models.Movie

data class MoviesViewModelState(val query: String?, val movies: List<Movie>?, val loadImages: Boolean?)