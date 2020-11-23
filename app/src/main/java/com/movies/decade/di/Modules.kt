package com.movies.decade.di

import com.movies.decade.businesslogic.MoviesManager
import com.movies.decade.businesslogic.MoviesRepository
import com.movies.decade.movieslist.MoviesListViewModel
import com.movies.decade.utils.FLICKR_BASE_URL
import me.linshen.retrofit2.adapter.LiveDataCallAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val applicationModule = module {
    factory { MoviesRepository(androidContext()) }
    factory { MoviesManager(androidContext()) }
    factory { MoviesListViewModel() }

    single {
        Retrofit.Builder()
            .baseUrl(FLICKR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}