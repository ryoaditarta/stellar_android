package com.example.stellar_android.components

import android.content.Context

object SnapRepository {
    private lateinit var database: SnapDatabase

    fun initDatabase(context: Context) {
        database = SnapDatabase.getAppDatabase(context)
    }

    suspend fun getSnaps(): List<Snap> {
        return database.snapDao().getAllSnaps()
    }

    suspend fun addSnap(snap: Snap) {
        database.snapDao().insertSnap(snap)
    }

    suspend fun deleteSnap(snap: Snap) {
        database.snapDao().deleteSnap(snap)
    }

    suspend fun updateSnap(snap: Snap) {
        database.snapDao().updateSnap(snap)  // New function to update a task
    }
}
