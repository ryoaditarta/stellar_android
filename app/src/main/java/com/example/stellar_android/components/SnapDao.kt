package com.example.stellar_android.components

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SnapDao {
    @Query("SELECT * FROM snaps")
    suspend fun getAllSnaps(): List<Snap>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnap(task: Snap)

    @Delete
    suspend fun deleteSnap(task: Snap)

    @Update
    suspend fun updateSnap(task: Snap)
}

