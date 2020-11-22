package com.movies.decade.movieslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.movies.decade.R
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.DbMovie
import com.movies.decade.movieslist.adapter.MoviesAdapter
import com.movies.decade.uimodels.MoviesUiModel
import kotlinx.android.synthetic.main.activity_movies_list.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.view_no_results.*

class MoviesListActivity : AppCompatActivity(), MoviesAdapter.Listener {

    private val viewModel: MoviesListViewModel by lazy {
        ViewModelProviders.of(this).get(MoviesListViewModel::class.java)
    }

    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)

        initRecyclerView()
        subscribeToMovies()
    }

    private fun initRecyclerView() {
        rvMovies.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MoviesAdapter(emptyList(), this)
        rvMovies.adapter = adapter
    }

    private fun subscribeToMovies() {
        viewModel.viewState.observe(this) {
            it?.let { render(it) }
        }
    }

    private fun render(uiModel: MoviesUiModel) {
        if (uiModel.movies == null)
            showNoResults()

        if (!uiModel.hasList)
            showLoading()
        else
            uiModel.movies?.let { showMovies(it) } ?: showNoResults()
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

    private fun showMovies(movies: List<AdapterItem<DbMovie>>) {
        llNoResults.visibility = View.GONE
        llLoading.visibility = View.GONE
        rvMovies.visibility = View.VISIBLE

        adapter.updateMovies(movies)
    }

    override fun onMovieSelected(movie: DbMovie) {
        TODO("Not yet implemented")
    }
}