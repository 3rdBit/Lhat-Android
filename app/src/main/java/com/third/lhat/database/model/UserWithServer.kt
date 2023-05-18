package com.third.lhat.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class UserWithServer (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "serverUserId"
    )
    val servers: List<Server>
)