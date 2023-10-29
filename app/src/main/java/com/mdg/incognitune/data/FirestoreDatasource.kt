package com.mdg.incognitune.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mdg.incognitune.model.SongRecord
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreDatasource @Inject constructor() : Datasource {
    val db = Firebase.firestore

    override suspend fun addSong(song: SongRecord): Result<Unit> {
        return kotlin.runCatching {
            suspendCoroutine {continuation ->
                db.collection(COLLECTION_SONGS)
                    .add(song)
                    .addOnSuccessListener {documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error adding document", exception)
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }

    private companion object{
        const val TAG = "Firestore"
        const val COLLECTION_SONGS = "songs"
    }
}