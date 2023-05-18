package com.third.lhat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.third.lhat.database.dao.ServerDao
import com.third.lhat.database.dao.UserDao
import com.third.lhat.database.model.Server
import com.third.lhat.database.model.User

@Database(entities = [User::class, Server::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun serverDao(): ServerDao
}