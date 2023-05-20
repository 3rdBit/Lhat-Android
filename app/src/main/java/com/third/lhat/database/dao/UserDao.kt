package com.third.lhat.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.third.lhat.database.model.User
import com.third.lhat.database.model.UserWithServer

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: User): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(vararg user: User): List<Long>

    @Query("SELECT * FROM user " +
            "WHERE username = :username " +
            "LIMIT 1")
    fun getUserByUsername(username: String): User?

    fun queryOrInsert(user: User): Long {
        val savedUser = getUserByUsername(user.username)
        savedUser?.let { return it.userId }
        return insert(user)
    }

    @Delete
    fun delete(user: User)

    @Delete
    fun deleteAll(vararg user: User)

    @Update
    fun update(user: User)

    @Update
    fun updateAll(vararg user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAll(vararg user: User): List<Long>

    @Transaction
    @Query("SELECT * FROM User " +
            "WHERE username = :username")
    fun getUserAndServersByUsername(username: String): List<UserWithServer>

    @Query("SELECT * FROM User")
    fun getAllUser(): List<User>
}