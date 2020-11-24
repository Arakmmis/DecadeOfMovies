package com.movies.decade.movieslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.movies.decade.R
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.moviedetails.MovieActivity
import com.movies.decade.movieslist.adapter.MovieViewHolder
import com.movies.decade.movieslist.adapter.MoviesAdapter
import com.movies.decade.statemodels.MoviesUiState
import com.movies.decade.utils.toJson
import kotlinx.android.synthetic.main.activity_movies_list.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.view_no_results.*
import org.koin.android.ext.android.inject

class MoviesListActivity : AppCompatActivity(), MovieViewHolder.Listener {

    private val viewModel: MoviesListViewModel by inject()

    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)

        subscribeToTextChanges()
        initRecyclerView()
        subscribeToMovies()
    }

    private fun subscribeToTextChanges() {
        etSearch.doAfterTextChanged { query ->
            query?.toString()?.let {
                viewModel.searchMovies(query = it)
                rvMovies.smoothScrollToPosition(0)
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        rvMovies.layoutManager = layoutManager

        adapter = MoviesAdapter(emptyList(), this)
        rvMovies.adapter = adapter

        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    AdapterItem.TYPE_YEAR -> 2
                    else -> 1
                }
            }
        }
    }

    private fun subscribeToMovies() {
        viewModel.viewState.observe(this) {
            it?.let { render(it) }
        }
    }

    private fun render(uiState: MoviesUiState) {
        if (uiState.movies == null && uiState.isLoading) {
            showLoading()
            etSearch.isEnabled = false
            return
        } else {
            etSearch.isEnabled = true
        }

        if (uiState.movies == null) {
            showNoResults()
            return
        }

        showMovies(uiState.movies)
    }

    private fun showNoResults() {
        llNoResults.visibility = View.VISIBLE
        llLoading.visibility = View.GONE
        rvMovies.visibility = View.GONE
    }

    private fun showLoading() {
        llNoResults.visibility = View.GONE
        llLoading.visibility = View.VISIBLE
        rvMovies.visibility = View.GONE
    }

    private fun showMovies(movies: List<AdapterItem<Movie>>) {
        llNoResults.visibility = View.GONE
        llLoading.visibility = View.GONE
        rvMovies.visibility = View.VISIBLE

        adapter.updateMovies(movies)
    }

    override fun onMovieSelected(movie: Movie) {
        MovieActivity.start(this, toJson(movie))
    }
}