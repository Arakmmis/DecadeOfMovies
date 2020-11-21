package com.movies.decade.movieslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.movies.decade.R

class MoviesListActivity : AppCompatActivity() {

    private val viewModel: MoviesListViewModel by lazy {
        ViewModelProviders.of(this).get(MoviesListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)
    }
}