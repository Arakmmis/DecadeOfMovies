package com.movies.decade.businesslogic.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movies.decade.utils.compareAlphabetically

@Entity(tableName = "movie")
data class DbMovie(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val year: Int,
    val cast: List<String>? = null,
    val genres: List<String>,
    val rating: Int,
    val imagesUrls: List<String>? = null
) {

    fun compareByRating(other: DbMovie): Int {
        return when {
            this.rating > other.rating -> 1
            this.rating < other.rating -> -1
            else -> this.title.compareAlphabetically(other.title)
        }
    }
}