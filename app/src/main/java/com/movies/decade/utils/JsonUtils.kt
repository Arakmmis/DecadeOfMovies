package com.movies.decade.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movies.decade.businesslogic.models.Movie
import java.io.IOException
import java.io.InputStream

fun getJsonFromAssets(context: Context, fileName: String): String {
    return try {
        val inputStream: InputStream = context.assets.open(fileName)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charsets.UTF_8)
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

fun toMovie(json: String): Movie? {
    if (json.isEmpty()) return null

    val gson = Gson()
    val type = object : TypeToken<Movie>() {}.type
    return gson.fromJson(json, type)
}

fun toJson(movie: Movie): String {
    val gson = Gson()
    val type = object : TypeToken<Movie>() {}.type
    return gson.toJson(movie, type)
}