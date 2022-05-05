package com.ktHat.Utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.findAll(pattern: Pattern): List<String> {
    val list = mutableListOf<String>()
    val matcher: Matcher = pattern.matcher(this)
    while (matcher.find()) {
        list.add(matcher.group())
    }
    return list
}

fun getTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
