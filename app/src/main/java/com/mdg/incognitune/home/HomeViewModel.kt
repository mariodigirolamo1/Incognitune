package com.mdg.incognitune.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdg.incognitune.common.model.SongRecord
import com.mdg.incognitune.firebaseauth.data.FirebaseAuthRepo
import com.mdg.incognitune.firestore.domain.AddSongRecordUseCase
import com.mdg.incognitune.firestore.domain.GetRandomSongUseCase
import com.mdg.incognitune.firestore.domain.GetSongsCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * todo: create ui state for Home
 *  we expecte it to contain info about the suggested song state
 *  at least let's show a loading and a ready
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuthRepo: FirebaseAuthRepo,
    private val addSongRecordUseCase: AddSongRecordUseCase,
    private val getSongsCountUseCase: GetSongsCountUseCase,
    private val getRandomSongUseCase: GetRandomSongUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val uiState = _uiState

    init {
        if(firebaseAuthRepo.isUserSignedIn()){
            getSongsCount()
            getRandomSong()
        }else{
            viewModelScope.launch {
                _uiState.emit(HomeUIState.UserNotSignedIn)
            }
        }
    }

    private fun getSongsCount(){
        // TODO: result handling can be put to common factors
        viewModelScope.launch(Dispatchers.IO){
            kotlin.runCatching {
                getSongsCountUseCase().getOrThrow()
            }.onSuccess {
                Log.i(TAG, "getSongsCount: $it songs found")
            }.onFailure {
                Log.e(TAG, "getSongsCount: failed", it)
            }
        }
    }

    private fun getRandomSong(){
        viewModelScope.launch(Dispatchers.IO){
            kotlin.runCatching {
                getRandomSongUseCase().getOrThrow()
            }.onSuccess {song ->
                val songLink = song.link
                Log.i(TAG, "getRandomSong: $songLink")
                _uiState.emit(HomeUIState.Ready(songLink = songLink))
            }.onFailure {throwable ->
                Log.e(TAG, "getRandomSong: failed", throwable)
            }
        }
    }

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