package com.mdg.incognitune.firestore.data

import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mdg.incognitune.common.data.Datasource
import com.mdg.incognitune.common.model.SongRecord
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

// TODO: this needs firebase auth otherwise no production green light
class FirestoreDatasource @Inject constructor() : Datasource {
    private val db = Firebase.firestore

    override suspend fun getAddedTodaySongsCount(userId: String?): Result<Long> {
        return kotlin.runCatching {
            val yesterdayThisHourMillis = System.currentTimeMillis() - 24 * 60 * 60 * 1000

            suspendCoroutine { continuation ->
                db.collection(COLLECTION_SONGS)
                    .whereEqualTo(SONG_RECORD_FIELD_ADDED_BY, userId)
                    .whereGreaterThanOrEqualTo(SONG_RECORD_FIELD_CREATION, yesterdayThisHourMillis)
                    .count()
                    .get(AggregateSource.SERVER)
                    .addOnSuccessListener { aggregateQuerySnapshot ->
                        Log.i(TAG, "getSongsUploadedToday: songs that users added today are ${aggregateQuerySnapshot.count}")
                        continuation.resume(aggregateQuerySnapshot.count)
                    }
                    .addOnFailureListener{ exception ->
                        Log.e(TAG, "getSongsUploadedToday: failed", exception)
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }

    override suspend fun addSong(song: SongRecord): Result<Unit> {
        return kotlin.runCatching {
            suspendCoroutine { continuation ->
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

    /**
     * Given a userId, retrieves a random inserted ONLY from userIds different from
     * the one provided.
     *
     * Use Case: a user will only receive song submitted by other users but not
     *           ones submitted by himself.
     */
    override suspend fun getRandomSong(
        userId: String?
    ): Result<SongRecord> {
        return kotlin.runCatching {
            val latestSongs = getLatestDocumentsFormOtherUsers(userId = userId!!).getOrThrow()
            // TODO: this random should be generated from a combination of
            //  userId and today's date to the day, so same day
            //  returns the same result every time
            val latestSong = latestSongs[Random.nextInt(latestSongs.size)]
            SongRecord(
                addedBy = latestSong.getString(SONG_RECORD_FIELD_ADDED_BY)!!,
                creation = latestSong.getLong(SONG_RECORD_FIELD_CREATION)!!,
                link = latestSong.getString(SONG_RECORD_FIELD_LINK)!!
            )
        }
    }

    private suspend fun getLatestDocumentsFormOtherUsers(
        userId: String
    ): Result<List<DocumentSnapshot>> {
        return kotlin.runCatching {
            val songsListSize = getSongsCount().getOrThrow()
            val fetchSongsLimit = 30
            val realSongsLimit = fetchSongsLimit
                .coerceAtMost(songsListSize.toInt())
                .toLong()

            val filter = Filter.notEqualTo(SONG_RECORD_FIELD_ADDED_BY,userId)

            suspendCoroutine { continuation ->
                db.collection(COLLECTION_SONGS)
                    .where(filter)
                    .orderBy(SONG_RECORD_FIELD_ADDED_BY)
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