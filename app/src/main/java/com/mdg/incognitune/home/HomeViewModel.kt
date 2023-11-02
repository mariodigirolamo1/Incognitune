package com.mdg.incognitune.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdg.incognitune.common.model.SongRecord
import com.mdg.incognitune.firebaseauth.data.FirebaseAuthRepo
import com.mdg.incognitune.firestore.domain.AddSongRecordUseCase
import com.mdg.incognitune.firestore.domain.GetRandomSongUseCase
import com.mdg.incognitune.firestore.domain.getSongsAddedTodayCountUseCase
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
    private val getRandomSongUseCase: GetRandomSongUseCase,
    private val getSongsAddedTodayCountUseCase: getSongsAddedTodayCountUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val uiState = _uiState

    init {  
        if(firebaseAuthRepo.isUserSignedIn()){
            viewModelScope.launch {
                val readyUiState = HomeUIState.Ready(songLink = "", hasSentDailySong = true)
                getRandomSong(uiState = readyUiState)
                getSongsAddedTodayCount(uiState = readyUiState)
                _uiState.emit(readyUiState)
            }
        }else{
            Log.i(TAG, "user is signed out")
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

    private suspend fun getSongsAddedTodayCount(
        uiState: HomeUIState.Ready
    ){
        kotlin.runCatching {
            getSongsAddedTodayCountUseCase().getOrThrow()
        }.onSuccess { dailyAddedSongsCount ->
            uiState.hasSentDailySong = dailyAddedSongsCount.toInt() != 0
            Log.i(TAG, "getSongsAddedTodayCount: this user added $dailyAddedSongsCount songs today")
        }.onFailure { throwable ->
            Log.e(TAG, "getSongsAddedTodayCount: failed", throwable)
        }
    }

    private suspend fun getRandomSong(
        uiState: HomeUIState.Ready
    ){
        kotlin.runCatching {
            getRandomSongUseCase().getOrThrow()
        }.onSuccess {song ->
            val songLink = song.link
            uiState.songLink = songLink
            Log.i(TAG, "getRandomSong: $songLink")
        }.onFailure {throwable ->
            Log.e(TAG, "getRandomSong: failed", throwable)
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
                        val currentUiState = uiState.value as HomeUIState.Ready
                        _uiState.emit(
                            HomeUIState.Ready(
                                songLink = currentUiState.songLink,
                                hasSentDailySong = true
                            )
                        )
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