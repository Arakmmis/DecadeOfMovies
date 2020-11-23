package com.movies.decade.businesslogic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movies.decade.businesslogic.models.ImagesResponse
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.businesslogic.models.RetrievedMovies
import com.movies.decade.utils.MOVIES_FILE_NAME
import com.movies.decade.utils.getJsonFromAssets
import io.reactivex.Single
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import java.lang.reflect.Type

class MoviesRepository(private val context: Context) {

    private val retrofit: Retrofit by inject(Retrofit::class.java)

    fun getMoviesFromFile(): LiveData<List<Movie>> {
        val observable = MutableLiveData<List<Movie>>()

        val jsonFileString: String =
            getJsonFromAssets(context, MOVIES_FILE_NAME)

        val listUserType: Type = object : TypeToken<RetrievedMovies>() {}.type

        observable.value = (Gson().fromJson(jsonFileString, listUserType) as RetrievedMovies).movies

        return observable
    }

    fun getMovieImageList(movie: Movie): Single<ImagesResponse> {
        return retrofit.create(FlickrApiService::class.java)
            .getMoviesImage(movie.title)
    }
}