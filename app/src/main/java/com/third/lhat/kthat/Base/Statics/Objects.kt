package com.ktHat.Statics

import com.ktHat.Utils.NullAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.third.lhat.ViewModel

object Objects{
    val moshi: Moshi = Moshi.Builder()
        .add(NullAdapter)
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val type = Types.newParameterizedType(List::class.java, String::class.java)
    val listAdapter: JsonAdapter<List<String>> = moshi.adapter(type)
    var viewModel = ViewModel()
}