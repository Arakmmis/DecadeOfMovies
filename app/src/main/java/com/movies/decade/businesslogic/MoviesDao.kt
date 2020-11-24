package com.movies.decade.businesslogic

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.decade.businesslogic.models.Movie

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(vararg movie: Movie): List<Long>

    @Query("SELECT * FROM movie ORDER BY year DESC, rating DESC, title ASC")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie " +
            "WHERE title LIKE '%' || :query  || '%' OR " +
            "year LIKE '%' || :query  || '%' OR " +
            "rating LIKE '%' || :query  || '%' OR " +
            "`cast` LIKE '%' || :query  || '%' OR " +
            "genres LIKE '%' || :query  || '%' ORDER BY year DESC, rating DESC, title ASC")
    fun getQueriedMovies(query: String): LiveData<List<Movie>>
}