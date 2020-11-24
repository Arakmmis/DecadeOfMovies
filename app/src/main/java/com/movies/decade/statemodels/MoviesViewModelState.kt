package com.movies.decade.statemodels

import com.movies.decade.businesslogic.models.Movie

data class MoviesViewModelState(val movies: List<Movie>?, val loadImages: Boolean?)