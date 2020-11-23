package com.movies.decade.utils

import com.movies.decade.businesslogic.models.AdapterItem
import org.junit.Test

class MovieUtilsTest {

    @Test
    fun `assert adapter list is greater than movie list`() {
        val initialMoviesSize = 10
        val adapterList = toAdapterList(getMovieList(initialMoviesSize))
        assert(adapterList.size > initialMoviesSize)
    }

    @Test
    fun `assert adapter list is list of movies separated by years`() {
        val initialMoviesSize = 10
        val adapterList = toAdapterList(getMovieList(initialMoviesSize))

        var currentItemType = adapterList[0].viewType

        adapterList.forEach { item ->
            if (currentItemType == AdapterItem.TYPE_YEAR) {
                if (item.viewType == AdapterItem.TYPE_YEAR) {
                    currentItemType = -1
                    return
                } else {
                    currentItemType = item.viewType
                }
            } else {
                currentItemType = item.viewType
            }
        }

        assert(currentItemType != -1)
    }
}