package com.movies.decade.movieslist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.movies.decade.businesslogic.models.Movie
import kotlinx.android.synthetic.main.view_year_header.view.*

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindYear(movie: Movie) = with(itemView) {
        tvYear.text = movie.year.toString()
    }
}