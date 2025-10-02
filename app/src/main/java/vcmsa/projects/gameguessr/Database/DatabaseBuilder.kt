package vcmsa.projects.gameguessr.Database

import android.content.Context
import androidx.room.Room
import vcmsa.projects.gameguessr.Database.AppDatabase

//this object ensures that the databaseis only created once
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
