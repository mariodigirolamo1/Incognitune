package com.mdg.incognitune.data

import com.mdg.incognitune.model.SongRecord
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestoreDatasource: FirestoreDatasource
) {
    suspend fun addSong(song: SongRecord): Result<Unit>{
        return firestoreDatasource.addSong(song)
    }
}