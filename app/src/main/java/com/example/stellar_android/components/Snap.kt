package com.example.stellar_android.components

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snaps")
data class Snap(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,  // Change 'val' to 'var' to allow reassignment
    val timestamp: Long,
    val filePath: String
)

