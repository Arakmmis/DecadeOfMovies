package com.movies.decade.businesslogic.models

data class AdapterItem<out T>(val value: T, val viewType: Int) {

    companion object {
        const val TYPE_YEAR = 0
        const val TYPE_MOVIE = 1
    }
}