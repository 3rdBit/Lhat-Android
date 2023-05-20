package com.third.lhat.dependency.kthat.base.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

object NullAdapter {
    @FromJson fun fromJson(reader: JsonReader): String {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextString()
        }
        reader.nextNull<Unit>()
        return ""
    }
}