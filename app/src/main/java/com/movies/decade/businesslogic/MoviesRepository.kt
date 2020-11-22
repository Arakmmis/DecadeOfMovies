package com.movies.decade.businesslogic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.businesslogic.models.RetrievedMovies
import com.movies.decade.utils.MOVIES_FILE_NAME
import com.movies.decade.utils.getJsonFromAssets
import java.lang.reflect.Type

class MoviesRepository(private val context: Context) {

    fun getMovies(): LiveData<List<Movie>> {
        val mutableLiveData = MutableLiveData<List<Movie>>()
        mutableLiveData.value = getMoviesFromFile()

        return mutableLiveData
    }

    private fun getMoviesFromFile(): List<Movie> {
        val jsonFileString: String =
            getJsonFromAssets(context, MOVIES_FILE_NAME)

        val listUserType: Type = object : TypeToken<RetrievedMovies>() {}.type

        return (Gson().fromJson(jsonFileString, listUserType) as RetrievedMovies).movies
    }
}