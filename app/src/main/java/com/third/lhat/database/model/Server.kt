package com.third.lhat.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.third.lhat.database.converter.ListConverter
import com.third.lhat.database.converter.LocalDateTimeConverter
import java.time.LocalDateTime

@Entity
@TypeConverters(LocalDateTimeConverter::class)
data class Server(
    @PrimaryKey(autoGenerate = true) val serverId: Int,
    val serverUserId: Int,
    val address: String,
    val port: Int,
    val lastLogin: LocalDateTime
)
