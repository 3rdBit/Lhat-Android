package com.third.lhat.database.converter

import androidx.room.TypeConverter
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTimeConverter {
    val zoneOffset = ZoneOffset.UTC
    @TypeConverter
    fun timeToTimestamp(time: LocalDateTime) = time.toInstant(zoneOffset).toEpochMilli()

    @TypeConverter
    fun timestampToTime(timestamp: Long) = Instant.ofEpochMilli(timestamp).atZone(zoneOffset).toLocalDateTime()
}