package com.mdg.incognitune.domain

import com.mdg.incognitune.data.FirestoreRepository
import javax.inject.Inject

class AddSongRecordUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    operator fun invoke(){

    }
}