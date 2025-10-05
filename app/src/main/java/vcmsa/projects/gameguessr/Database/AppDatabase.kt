package vcmsa.projects.gameguessr.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import vcmsa.projects.gameguessr.Class.Game
import vcmsa.projects.gameguessr.Class.GameConverters
import vcmsa.projects.gameguessr.DAOs.GameDAO

//this is the database, whatever you need changed you have to update here
//update the version by 1, everytime you make a change
@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(GameConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDAO
}