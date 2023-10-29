package com.mdg.incognitune.firestore.data

import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mdg.incognitune.common.data.Datasource
import com.mdg.incognitune.common.model.SongRecord
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

// TODO: this needs firebase auth otherwise no production green light
class FirestoreDatasource @Inject constructor() : Datasource {
    private val db = Firebase.firestore

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

    override suspend fun getRandomSong(): Result<SongRecord> {
        return kotlin.runCatching {
            val latestSongs = getLatestDocuments().getOrThrow()
            val latestSong = latestSongs[Random.nextInt(latestSongs.size)]
            SongRecord(
                addedBy = latestSong.getString(SONG_RECORD_FIELD_ADDED_BY)!!,
                creation = latestSong.getLong(SONG_RECORD_FIELD_CREATION)!!,
                link = latestSong.getString(SONG_RECORD_FIELD_LINK)!!
            )
        }
    }

    private suspend fun getLatestDocuments(): Result<List<DocumentSnapshot>> {
        return kotlin.runCatching {
            val songsListSize = getSongsCount().getOrThrow()
            val fetchSongsLimit = 30
            val realSongsLimit = fetchSongsLimit
                .coerceAtMost(songsListSize.toInt())
                .toLong()

            suspendCoroutine { continuation ->
                db.collection(COLLECTION_SONGS)
                    .orderBy(SONG_RECORD_FIELD_CREATION)
                    .limitToLast(realSongsLimit)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        continuation.resume(querySnapshot.documents)
                    }.addOnFailureListener{exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }

    override suspend fun getSongsCount(): Result<Long> {
        return kotlin.runCatching {
            suspendCoroutine { continuation ->
                db.collection(COLLECTION_SONGS)
                    .count()
                    .get(AggregateSource.SERVER)
                    .addOnSuccessListener { querySnapshot ->
                        continuation.resume(querySnapshot.count)
                    }.addOnFailureListener{exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }

    private companion object{
        const val TAG = "Firestore"
        const val COLLECTION_SONGS = "songs"
        const val SONG_RECORD_FIELD_ADDED_BY = "addedBy"
        const val SONG_RECORD_FIELD_CREATION = "creation"
        const val SONG_RECORD_FIELD_LINK = "link"
    }
}