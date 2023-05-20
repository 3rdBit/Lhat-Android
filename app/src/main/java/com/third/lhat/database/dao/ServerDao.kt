package com.third.lhat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.third.lhat.database.model.Server

@Dao
interface ServerDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(server: Server): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(vararg server: Server): List<Long>

    @Update
    fun update(server: Server)

    @Query("SELECT * FROM server")
    fun queryAll(): List<Server>

    @Query(
        "SELECT * FROM server " +
                "WHERE serverUserId = :userId " +
                "AND address = :address " +
                "AND port = :port"
    )
    fun getServerByAddressPortWithUserId(
        address: String,
        port: Int,
        userId: Long
    ): Server?

    fun updateOrInsert(server: Server): Long {
        val savedServer = getServerByAddressPortWithUserId(
            address = server.address,
            port = server.port,
            userId = server.serverUserId
        )
        savedServer?.let {
            update(savedServer.copy(lastLogin = server.lastLogin))
            return it.serverId
        }
        return insert(server)
    }
}