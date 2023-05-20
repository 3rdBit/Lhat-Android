package com.third.lhat.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.third.lhat.database.dao.ServerDao
import com.third.lhat.database.dao.UserDao
import com.third.lhat.database.model.Server
import com.third.lhat.database.model.User

@Database(
    entities = [User::class, Server::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun serverDao(): ServerDao
}