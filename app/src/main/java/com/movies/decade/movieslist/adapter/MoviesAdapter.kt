package com.movies.decade.movieslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.movies.decade.R
import com.movies.decade.businesslogic.models.AdapterItem
import com.movies.decade.businesslogic.models.Movie
import kotlinx.android.synthetic.main.view_movie.view.*
import kotlinx.android.synthetic.main.view_year_header.view.*

class MoviesAdapter(private var items: List<AdapterItem<Movie>>, private val listener: Listener) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = if (viewType == AdapterItem.TYPE_YEAR) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_year_header, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_movie, parent, false)
        }

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items[position].viewType == AdapterItem.TYPE_MOVIE)
            holder.bindMovie(items[position].value, listener)
        else
            holder.bindYear(items[position].value)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].viewType

    fun updateMovies(items: List<AdapterItem<Movie>>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindMovie(movie: Movie, listener: Listener) = with(itemView) {
            try {
                Glide.with(context)
                    .load(movie.imagesUrls?.get(0))
                    .placeholder(R.drawable.ic_movie_poster_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivMoviePoster)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            tvMovieTitle.text = movie.title

            setOnClickListener {
                listener.onMovieSelected(movie)
            }
        }

        fun bindYear(movie: Movie) = with(itemView) {
            tvYear.text = movie.year.toString()
        }
    }

    interface Listener {
        fun onMovieSelected(movie: Movie)
    }
}