@file:Suppress("unused")

package com.third.lhat.database.converter

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType

class ListConverter {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val type: ParameterizedType = Types.newParameterizedType(List::class.java)

    private val adapter: JsonAdapter<List<*>> = moshi.adapter(type)

    @TypeConverter
    fun jsonToList(json: String): List<*> =
        adapter.fromJson(json)!!

    @TypeConverter
    fun listToJson(list: List<*>) =
        adapter.toJson(list)
}