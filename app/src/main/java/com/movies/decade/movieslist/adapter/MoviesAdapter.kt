package com.movies.decade.movieslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movies.decade.R
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie

class MoviesAdapter(
    private var items: List<AdapterItem<Movie>>,
    private val listener: MovieViewHolder.Listener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = if (viewType == AdapterItem.TYPE_YEAR) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_year_header, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_movie, parent, false)
        }

        return when (viewType) {
            AdapterItem.TYPE_YEAR -> HeaderViewHolder(itemView)
            else -> MovieViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items[position].viewType == AdapterItem.TYPE_MOVIE)
            (holder as MovieViewHolder).bindMovie(items[position].value, listener)
        else
            (holder as HeaderViewHolder).bindYear(items[position].value)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].viewType

    fun updateMovies(items: List<AdapterItem<Movie>>) {
        this.items = items
        notifyDataSetChanged()
    }
}