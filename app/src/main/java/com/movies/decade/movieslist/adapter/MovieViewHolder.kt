package com.movies.decade.movieslist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movies.decade.R
import com.movies.decade.businesslogic.models.Movie
import kotlinx.android.synthetic.main.view_movie.view.*

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindMovie(movie: Movie, listener: Listener) = with(itemView) {
        try {
            if (movie.imagesUrls?.isNotEmpty() == true) {
                movie.imagesUrls?.get(0)?.let {
                    Glide.with(this)
                        .load(it)
                        .placeholder(R.drawable.ic_movie_poster_placeholder)
                        .into(ivMoviePoster)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tvMovieTitle.text = movie.title

        setOnClickListener {
            listener.onMovieSelected(movie)
        }
    }


    interface Listener {
        fun onMovieSelected(movie: Movie)
    }
}