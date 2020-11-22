package com.movies.decade.di

import com.movies.decade.businesslogic.MoviesDao
import com.movies.decade.businesslogic.MoviesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val applicationModule = module {
    factory { MoviesRepository(androidContext()) }
    factory { MoviesDao() }
}