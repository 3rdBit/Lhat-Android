package com.third.lhat.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.third.lhat.database.converter.LocalDateTimeConverter
import java.time.LocalDateTime

@Entity
@TypeConverters(LocalDateTimeConverter::class)
data class Server(
    @PrimaryKey(autoGenerate = true) val serverId: Long,
    val serverUserId: Long,
    val address: String,
    val port: Int,
    val lastLogin: LocalDateTime
)
