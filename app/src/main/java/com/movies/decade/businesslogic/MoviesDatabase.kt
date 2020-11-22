package com.movies.decade.businesslogic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movies.decade.businesslogic.models.DbMovie
import com.movies.decade.utils.TypeConverter

@Database(entities = [DbMovie::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {

        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}