package com.mdg.incognitune.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdg.incognitune.common.model.SongRecord
import com.mdg.incognitune.firestore.domain.AddSongRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addSongRecordUseCase: AddSongRecordUseCase
) : ViewModel() {
    fun addSong(){
        val song = SongRecord(
            addedBy = UUID.randomUUID().toString(),
            creation = System.currentTimeMillis(),
            link = "somerandomlink"
        )
        // TODO: inject dispatchers for testing
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                addSongRecordUseCase(song = song).getOrThrow()
            }.onSuccess {
                Log.i(TAG, "addSong: success")
            }.onFailure { throwable ->
                Log.e(TAG, "addSong: failed", throwable)
            }
        }
    }

    private companion object{
        const val TAG = "HomeViewModel"
    }
}