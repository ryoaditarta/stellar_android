package com.example.stellar_android.components

import com.example.stellar_android.dataclass.Snap

class SnapRepository(private val snapDao: SnapDao) {

    suspend fun insertSnap(snap: Snap) {
        snapDao.insert(snap)
    }

    suspend fun getAllSnaps(): List<Snap> {
        return snapDao.getAllSnaps()
    }
}
