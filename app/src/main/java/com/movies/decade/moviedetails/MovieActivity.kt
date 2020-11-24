package com.movies.decade.moviedetails

import android.R.attr.scrollY
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.movies.decade.R
import com.movies.decade.moviedetails.adapter.GalleryPhotoViewHolder
import com.movies.decade.moviedetails.adapter.GenreViewHolder
import com.movies.decade.moviedetails.adapter.StarViewHolder
import com.movies.decade.moviedetails.adapter.TypeAdapter
import com.movies.decade.utils.toMovie
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.content_movie.*


class MovieActivity : AppCompatActivity() {

    val movie by lazy {
        toMovie(intent.getStringExtra(MOVIE_JSON_STRING) ?: "")
    }

    companion object {
        private const val MOVIE_JSON_STRING = "movie"

        fun start(context: Context, movie: String) {
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra(MOVIE_JSON_STRING, movie)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        initView()
        initRecyclerViews()
    }

    private fun initView() {
        if (movie?.imagesUrls?.isNotEmpty() == true) {
            movie?.imagesUrls?.get(0)?.let {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_movie_poster_placeholder)
                    .into(ivMoviePoster)
            }
        }

        tvMovieTitle.text = movie?.title
        tvReleaseDate.text = movie?.year?.toString()

        tvRating.text = String.format("%d/10", movie?.rating)
    }

    private fun initRecyclerViews() {
        initGenreAdapter(movie?.genres)
        initCastAdapter(movie?.cast)
        initGalleryAdapter(movie?.imagesUrls)
    }

    private fun initGenreAdapter(genres: List<String>?) {
        if (genres.isNullOrEmpty()) {
            rvGenres.visibility = View.GONE
            return
        }

        rvGenres.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvGenres.adapter = TypeAdapter(
            R.layout.view_genre,
            genres,
            GenreViewHolder::class.java
        )
    }

    private fun initCastAdapter(cast: List<String>?) {
        if (cast.isNullOrEmpty()) {
            tvCast.visibility = View.GONE
            rvCast.visibility = View.GONE
            sepCast.visibility = View.GONE
            return
        }

        rvCast.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvCast.adapter = TypeAdapter(
            R.layout.view_star,
            cast,
            StarViewHolder::class.java
        )
    }

    private fun initGalleryAdapter(images: List<String>?) {
        if (images.isNullOrEmpty()) {
            tvGallery.visibility = View.GONE
            rvGallery.visibility = View.GONE
            sepGallery.visibility = View.GONE
            return
        }

        rvGallery.layoutManager = GridLayoutManager(this, 2)
        rvGallery.adapter = TypeAdapter(
            R.layout.view_movie_photo,
            images,
            GalleryPhotoViewHolder::class.java
        )
    }
}