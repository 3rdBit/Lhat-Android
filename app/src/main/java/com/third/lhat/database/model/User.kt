package com.third.lhat.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.third.lhat.database.converter.ListConverter

@Entity
//    (foreignKeys = [ForeignKey(
//    entity = Server::class,
//    parentColumns = ["serverId"],
//    childColumns = ["id"],
//    onDelete = ForeignKey.CASCADE
//)])
data class User(
    @PrimaryKey val userId: Int,
//    val userServerId: Int,
    val username: String
)
