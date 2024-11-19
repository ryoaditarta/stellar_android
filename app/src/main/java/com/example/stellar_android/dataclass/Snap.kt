package com.example.stellar_android.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snaps")
data class Snap(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val judul: String,
    val fotoUri: String // Store the file path or URI of the image
)
