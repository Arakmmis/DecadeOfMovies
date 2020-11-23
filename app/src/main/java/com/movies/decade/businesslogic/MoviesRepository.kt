package com.movies.decade.businesslogic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.businesslogic.models.RetrievedMovies
import com.movies.decade.utils.getFlickrImageUrl
import com.movies.decade.utils.getJsonFromAssets
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.*

class MoviesRepository(private val context: Context) {

    private val retrofit: Retrofit by inject(Retrofit::class.java)

    fun getMoviesFromFile(fileName: String): LiveData<List<Movie>> {
        val observable = MutableLiveData<List<Movie>>()

        val jsonFileString: String =
            getJsonFromAssets(context, fileName)

        val listUserType: Type = object : TypeToken<RetrievedMovies>() {}.type

        observable.value = (Gson().fromJson(jsonFileString, listUserType) as RetrievedMovies).movies

        return observable
    }

    fun getMoviesImages(movies: List<Movie>): List<Movie> {
        return movies.map { getMovieImageList(it) }
    }

    private fun getMovieImageList(movie: Movie): Movie {
        val disposable = CompositeDisposable()

        disposable.add(
            retrofit.create(FlickrApiService::class.java)
                .getMoviesImage(movie.title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { images, e ->
                    if (e != null) {
                        Log.e("MR", e.localizedMessage)
                        disposable.clear()
                        return@subscribe
                    }

                    val imageUrlList = ArrayList<String>()

                    images?.wrapper?.photos?.forEach { photo ->
                        val url = getFlickrImageUrl(photo)
                        imageUrlList.add(url)
                    }

                    movie.imagesUrls = imageUrlList

                    disposable.clear()
                }
        )

        return movie
    }
}