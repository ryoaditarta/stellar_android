package com.example.stellar_android.components

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Snap::class], version = 1, exportSchema = false)
abstract class SnapDatabase : RoomDatabase() {
    abstract fun snapDao(): SnapDao

    companion object {
        @Volatile
        private var INSTANCE: SnapDatabase? = null

        fun getAppDatabase(context: Context): SnapDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SnapDatabase::class.java,
                    "my_database_name"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
