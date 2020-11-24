package com.movies.decade.utils

import org.junit.Test

class StringUtilsTest {

    private val firstStr = "First String"
    private val secondStr = "Second String"

    @Test
    fun checkIfCallerComesBeforeComparedToReturnOne() {
        assert(firstStr.compareAlphabeticallyTo(secondStr) == 1)
    }

    @Test
    fun checkIfCallerEqualsComparedToReturnOne() {
        assert(firstStr.compareAlphabeticallyTo(firstStr) == 1)
    }

    @Test
    fun checkIfCallerComesAfterComparedToReturnNegativeOne() {
        assert(secondStr.compareAlphabeticallyTo(firstStr) == -1)
    }
}