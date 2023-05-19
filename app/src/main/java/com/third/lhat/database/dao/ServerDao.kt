package com.third.lhat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.third.lhat.database.model.Server

@Dao
interface ServerDao {
    @Insert
    fun insert(server: Server): Long

    @Insert
    fun insertAll(vararg server: Server): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(server: Server): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAll(vararg server: Server): List<Long>

    @Query("SELECT * from Server")
    fun queryAll(): List<Server>
}