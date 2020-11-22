package com.movies.decade.uimodels

import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.DbMovie

data class MoviesUiModel(val movies: List<AdapterItem<DbMovie>>?, val hasList: Boolean)