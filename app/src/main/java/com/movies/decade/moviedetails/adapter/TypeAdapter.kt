package com.movies.decade.moviedetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TypeAdapter<out T, V : RecyclerView.ViewHolder>(
    private val layout: Int,
    private var items: List<T>,
    private val clazz: Class<V>
) :
    RecyclerView.Adapter<V>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return when (clazz) {
            StarViewHolder::class.java -> StarViewHolder(view) as V
            GenreViewHolder::class.java -> GenreViewHolder(view) as V
            GalleryPhotoViewHolder::class.java -> GalleryPhotoViewHolder(view) as V
            else -> throw Exception("Unhandled ViewHolder Type")
        }
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        when (holder) {
            is StarViewHolder -> {
                (holder as StarViewHolder).bind(items[position])
            }

            is GenreViewHolder -> {
                (holder as GenreViewHolder).bind(items[position])
            }

            is GalleryPhotoViewHolder -> {
                (holder as GalleryPhotoViewHolder).bind(items[position])
            }

            else -> throw Exception("Unhandled ViewHolder Type")
        }
    }

    override fun getItemCount(): Int = items.size
}