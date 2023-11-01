package com.mdg.incognitune.common.data

import com.mdg.incognitune.common.model.SongRecord

interface Datasource {
    suspend fun getAddedTodaySongsCount(userId: String?): Result<Long>
    suspend fun addSong(song: SongRecord): Result<Unit>
    suspend fun getRandomSong(userId: String?): Result<SongRecord>
    suspend fun getSongsCount(): Result<Long>
}