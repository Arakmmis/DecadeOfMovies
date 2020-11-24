package com.movies.decade.utils

fun String.compareAlphabeticallyTo(other: String): Int {
    this.forEachIndexed { index, char ->
        return when {
            char > other[index] -> -1
            else -> 1
        }
    }

    return 1
}
