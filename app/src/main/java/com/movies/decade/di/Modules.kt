package com.movies.decade.di

import com.movies.decade.businesslogic.MoviesDatabase
import com.movies.decade.businesslogic.MoviesManager
import com.movies.decade.businesslogic.MoviesRepository
import com.movies.decade.movieslist.MoviesListViewModel
import com.movies.decade.utils.FLICKR_BASE_URL
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val applicationModule = module {
    factory { MoviesRepository(androidContext()) }

    factory { MoviesDatabase.getDatabase(androidContext()).moviesDao() }

    factory {
        MoviesManager(
            MoviesDatabase.getDatabase(androidContext()).moviesDao(),
            MoviesRepository(androidContext())
        )
    }

    factory {
        MoviesListViewModel(
            MoviesManager(
                MoviesDatabase.getDatabase(androidContext()).moviesDao(),
                MoviesRepository(androidContext())
            )
        )
    }

    single {
        Retrofit.Builder()
            .baseUrl(FLICKR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}