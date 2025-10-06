package com.example.gameguesser.DAOs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gameguesser.Class.User

@Dao
interface UserDao {

    // Insert a new user. If the user already exists, ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    // Get a user by their Google user ID
    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUser(id: String): User?

    // Update user data (like streak)
    @Update
    suspend fun updateUser(user: User)
}