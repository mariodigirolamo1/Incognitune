package com.mdg.incognitune.firestore.data

import com.mdg.incognitune.common.model.SongRecord
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestoreDatasource: FirestoreDatasource
) {
    suspend fun addSong(song: SongRecord): Result<Unit>{
        return firestoreDatasource.addSong(song)
    }

    suspend fun getRandomSong(): Result<SongRecord>{
        return firestoreDatasource.getRandomSong()
    }

    suspend fun getSongsCount(): Result<Long>{
        return firestoreDatasource.getSongsCount()
    }
}