package com.movies.decade.businesslogic

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.decade.businesslogic.models.DbMovie

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(vararg movie: DbMovie): List<Long>

    @Query("SELECT * FROM movie ORDER BY (year AND rating) ASC")
    fun getAllMovies(): LiveData<List<DbMovie>>

    @Query("SELECT * FROM movie WHERE (title OR `cast` OR year OR genres) LIKE :query ORDER BY (year AND rating) ASC")
    fun getQueriedMovies(query: String): LiveData<List<DbMovie>>

    @Query("DELETE FROM movie")
    fun removeAllMovies()
}