package com.ktHat.Utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
