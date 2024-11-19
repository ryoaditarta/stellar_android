package com.example.stellar_android.components

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stellar_android.dataclass.Snap

@Database(entities = [Snap::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun snapDao(): SnapDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "snaps_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
