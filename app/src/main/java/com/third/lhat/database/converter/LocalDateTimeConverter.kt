package com.third.lhat.database.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTimeConverter {
    private val zoneOffset: ZoneOffset = ZoneOffset.UTC
    @TypeConverter
    fun timeToTimestamp(time: LocalDateTime) = time.toInstant(zoneOffset).toEpochMilli()

    @TypeConverter
    fun timestampToTime(timestamp: Long): LocalDateTime = Instant.ofEpochMilli(timestamp).atZone(zoneOffset).toLocalDateTime()
}