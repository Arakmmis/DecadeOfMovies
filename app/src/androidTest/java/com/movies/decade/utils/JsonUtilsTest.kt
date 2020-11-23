package com.movies.decade.utils

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.inject

@RunWith(AndroidJUnit4::class)
class JsonUtilsTest {

    private val context: Context by inject(Context::class.java)
    private val fileName = MOVIES_FILE_NAME

    @Test
    fun checkIfFileNameIsEmptyReturnEmptyString() {
        assert(getJsonFromAssets(context, "") == "")
    }

    @Test
    fun checkIfFileNameIsWrongReturnEmptyString() {
        assert(getJsonFromAssets(context, "file.json") == "")
    }

    @Test
    fun checkIfFileNameIsRightReturnNonEmptyString() {
        assert(getJsonFromAssets(context, fileName).isNotEmpty())
    }
}