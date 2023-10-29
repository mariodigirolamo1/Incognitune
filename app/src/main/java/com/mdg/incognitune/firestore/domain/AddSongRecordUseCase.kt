package com.mdg.incognitune.firestore.domain

import com.mdg.incognitune.firestore.data.FirestoreRepository
import com.mdg.incognitune.common.model.SongRecord
import javax.inject.Inject

class AddSongRecordUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(
        song: SongRecord
    ): Result<Unit> {
        return firestoreRepository.addSong(song = song)
    }
}