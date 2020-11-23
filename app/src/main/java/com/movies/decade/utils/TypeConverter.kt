package com.movies.decade.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {

    @TypeConverter
    fun fromStringListToJsonString(list: List<String>?): String {
        if (list.isNullOrEmpty()) return ""

        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun fromJsonStringToStringList(json: String?): List<String> {
        if (json.isNullOrEmpty()) return emptyList()

        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}