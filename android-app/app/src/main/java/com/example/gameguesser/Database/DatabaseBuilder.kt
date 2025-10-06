package com.example.gameguesser.Database.DatabaseBuilder

import android.content.Context
import androidx.room.Room
import com.example.gameguesser.Database.AppDatabase

//this object ensures that the database is only created once
//and because of it, everyone will be reusing the same db
object DatabaseBuilder {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "gameguessr_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
