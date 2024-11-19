package com.example.stellar_android.components

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stellar_android.dataclass.Snap

@Dao
interface SnapDao {
    @Insert
    suspend fun insert(snap: Snap)

    @Query("SELECT * FROM snaps")
    suspend fun getAllSnaps(): List<Snap>
}
