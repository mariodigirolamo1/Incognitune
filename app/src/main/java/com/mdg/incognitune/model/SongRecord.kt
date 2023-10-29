package com.mdg.incognitune.model

import java.time.LocalDate

data class SongRecord(
    val addedBy: String,
    val creation: LocalDate,
    val link: String
)
