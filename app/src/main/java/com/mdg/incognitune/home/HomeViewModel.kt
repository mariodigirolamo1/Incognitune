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

    fun signOut(){
        viewModelScope.launch {
            firebaseAuthRepo.signOut()
            _uiState.emit(HomeUIState.UserNotSignedIn)
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

    fun addSong(
        songLink: String
    ){
        if(isValidSongLink(songLink = songLink)){
            firebaseAuthRepo.getUserId()?.let{ userId ->
                val song = SongRecord(
                    addedBy = userId,
                    creation = System.currentTimeMillis(),
                    link = songLink
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
        }else{
            Log.e(TAG, "addSong: provided url is invalid")
        }
    }

    private fun isValidSongLink(
        songLink: String
    ): Boolean {
        return if(songLink == "") false
        else if(songLink.startsWith("https://www.youtube.com/")) true
        else songLink.startsWith("https://open.spotify.com/")
    }

    private companion object{
        const val TAG = "HomeViewModel"
    }
}