package com.mdg.incognitune.firestore.data

import com.mdg.incognitune.common.model.SongRecord
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestoreDatasource: FirestoreDatasource
) {
    suspend fun addSong(song: SongRecord): Result<Unit>{
        return firestoreDatasource.addSong(song)
    }

    suspend fun getRandomSong(userId: String?): Result<SongRecord>{
        return firestoreDatasource.getRandomSong(userId = userId)
    }

    suspend fun getSongsCount(): Result<Long>{
        return firestoreDatasource.getSongsCount()
    }
}