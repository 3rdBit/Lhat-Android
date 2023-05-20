package com.third.lhat.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Chat(
    @PrimaryKey(autoGenerate = true) val chatId: Long,
    val chatUserId: Long,

)