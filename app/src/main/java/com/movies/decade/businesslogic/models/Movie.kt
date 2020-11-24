package com.movies.decade.businesslogic.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.movies.decade.utils.compareAlphabeticallyTo

@Entity(tableName = "movie", primaryKeys = ["title", "year"])
data class Movie(
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("year")
    @Expose
    val year: Int,
    @SerializedName("cast")
    @Expose
    val cast: List<String>? = null,
    @SerializedName("genres")
    @Expose
    val genres: List<String>? = null,
    @SerializedName("rating")
    @Expose
    val rating: Int,
    var imagesUrls: List<String>? = null) {

    fun compareByRating(other: Movie): Int {
        return when {
            this.rating > other.rating -> 1
            this.rating < other.rating -> -1
            else -> this.title.compareAlphabeticallyTo(other.title)
        }
    }
}