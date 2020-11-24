package com.movies.decade.moviedetails.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_genre.view.*

class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun <T> bind(item: T) = with(itemView) {
        tvGenre.text = item as String
    }
}