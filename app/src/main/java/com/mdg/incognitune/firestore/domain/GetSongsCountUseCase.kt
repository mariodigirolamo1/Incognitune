package com.mdg.incognitune.firestore.domain

import com.mdg.incognitune.firestore.data.FirestoreRepository
import javax.inject.Inject

class GetSongsCountUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(): Result<Long> {
        return firestoreRepository.getSongsCount()
    }
}