package com.movies.decade.moviedetails.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movies.decade.R
import kotlinx.android.synthetic.main.view_movie_photo.view.*

class GalleryPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun <T> bind(item: T) = with(itemView) {
        val photo = item as String

        Glide.with(this)
            .load(photo)
            .placeholder(R.drawable.ic_movie_poster_placeholder)
            .into(ivPhoto)
    }
}