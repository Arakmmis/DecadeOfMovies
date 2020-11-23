package com.movies.decade.movieslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.movies.decade.R
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie
import com.movies.decade.movieslist.adapter.MoviesAdapter
import com.movies.decade.statemodels.MoviesUiState
import kotlinx.android.synthetic.main.activity_movies_list.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.view_no_results.*
import org.koin.android.ext.android.inject

class MoviesListActivity : AppCompatActivity(), MoviesAdapter.Listener {

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
            query?.toString()?.let { viewModel.searchMovies(query = it) }
        }
    }

    private fun initRecyclerView() {
        rvMovies.layoutManager =
            GridLayoutManager(this, 2)
        adapter = MoviesAdapter(emptyList(), this)
        rvMovies.adapter = adapter
    }

    private fun subscribeToMovies() {
        viewModel.viewState.observe(this) {
            it?.let { render(it) }
        }
    }

    private fun render(uiState: MoviesUiState) {
        if (uiState.movies == null) {
            showNoResults()
            return
        }

        if (!uiState.hasList)
            showLoading()
        else
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
        TODO("Not yet implemented")
    }
}