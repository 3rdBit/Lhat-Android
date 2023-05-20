package com.third.lhat

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import androidx.room.Room
import com.third.lhat.dependency.kthat.base.utils.NullAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.third.lhat.database.AppDatabase

object Objects{
    val moshi: Moshi = Moshi.Builder()
        .add(NullAdapter)
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val type = Types.newParameterizedType(List::class.java, String::class.java)
    val listAdapter: JsonAdapter<List<String>> = moshi.adapter(type)
    var viewModel: ViewModel = ViewModel()
    var composeActivityInstance: ViewModelStoreOwner? = null
}

object Database {
    lateinit var applicationContext: Context
    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).build()
    }
}