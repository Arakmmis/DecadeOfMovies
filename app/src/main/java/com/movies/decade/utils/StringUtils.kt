package com.movies.decade.utils

fun String.alphabetized() = String(toCharArray().apply { sorted() })

fun String.compareAlphabetically(other: String): Int {
    this.alphabetized().forEachIndexed { index, char ->
        return when {
            char < other[index] -> -1
            else -> 1
        }
    }

    return 1
}
