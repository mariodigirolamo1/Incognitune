package com.mdg.incognitune.firestore.domain

import com.mdg.incognitune.common.model.SongRecord
import com.mdg.incognitune.firebaseauth.data.FirebaseAuthRepo
import com.mdg.incognitune.firestore.data.FirestoreRepository
import javax.inject.Inject

class GetRandomSongUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseAuthRepo: FirebaseAuthRepo
) {
    suspend operator fun invoke(): Result<SongRecord> {
        return firestoreRepository.getRandomSong(firebaseAuthRepo.getUserId())
    }
}