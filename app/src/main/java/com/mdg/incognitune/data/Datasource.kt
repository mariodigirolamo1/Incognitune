package com.mdg.incognitune.data

import com.mdg.incognitune.model.SongRecord

interface Datasource {
    suspend fun addSong(song: SongRecord): Result<Unit>
}