package com.example.stellar_android.components

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snaps")
data class Snap(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val timestamp: Long,
    val filePath: String
)
