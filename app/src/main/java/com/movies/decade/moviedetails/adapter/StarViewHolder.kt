package com.movies.decade.moviedetails.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.movies.decade.R
import kotlinx.android.synthetic.main.view_star.view.*
import java.util.*
import kotlin.collections.ArrayList

class StarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun <T> bind(item: T) = with(itemView) {
        val starName = item as String

        tvStarName.text = starName

        val nameSeparated = starName.split(" ")

        var starInitials = ""

        nameSeparated.forEachIndexed { index, name ->
            if (index > 1) return@forEachIndexed
            starInitials += name.first().toUpperCase()
        }

        tvStarInitials.text = starInitials
        tvStarInitials.background.setTint(getRandomColor())
    }

    private fun getRandomColor(): Int = with(itemView) {
        val colors = ArrayList<Int>()

        colors.add(ContextCompat.getColor(context, R.color.green_smoke))
        colors.add(ContextCompat.getColor(context, R.color.brandy))
        colors.add(ContextCompat.getColor(context, R.color.blue_marguerite))
        colors.add(ContextCompat.getColor(context, R.color.jacarta))
        colors.add(ContextCompat.getColor(context, R.color.heath))

        colors.random()
    }
}