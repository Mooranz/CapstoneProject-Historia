package com.tugas.capstoneproject_historia.data.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity

@Database(
    entities = [HistoryEntity::class],
    version = 1,
/*    autoMigrations = [
        AutoMigration(from = 1, to = 2)
                ],*/
    exportSchema = false
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var instance: HistoryDatabase? = null
        fun getInstance(context: Context): HistoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java, "News.db"
                ).build()
            }
    }
}