package com.example.gameguesser.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gameguesser.data.Game
import com.example.gameguesser.DAOs.GameDAO.GameDao
import com.example.gameguesser.data.GameConverters

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(GameConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "game_db"
                )
                    // DO NOT add a GameConverters instance here. The @TypeConverters annotation above is sufficient.
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
