package com.third.lhat.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
//    foreignKeys = [ForeignKey(
//        entity = Server::class,
//        parentColumns = ["serverId"],
//        childColumns = ["id"],
//        onDelete = ForeignKey.CASCADE
//    )],
    indices = [Index(value = ["username"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long,
//    val userServerId: Long,
    val username: String
)
