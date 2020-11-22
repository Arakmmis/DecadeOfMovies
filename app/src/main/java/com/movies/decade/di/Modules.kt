package com.movies.decade.di

import com.movies.decade.businesslogic.MoviesRepository
import com.movies.decade.movieslist.MoviesListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule = module {
    factory { MoviesRepository(androidContext()) }
    factory { MoviesListViewModel(androidContext()) }
}