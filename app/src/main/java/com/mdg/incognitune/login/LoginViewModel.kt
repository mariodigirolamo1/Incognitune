package com.mdg.incognitune.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdg.incognitune.firebaseauth.data.FirebaseAuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuthRepo: FirebaseAuthRepo
): ViewModel() {
    private var _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Loading)
    val uiState = _uiState

    init {
        firebaseAuthRepo.initAuth()
        viewModelScope.launch {
            if(firebaseAuthRepo.isUserSignedIn()){
                _uiState.emit(LoginUIState.LoginSuccessful)
            }else{
                _uiState.emit(LoginUIState.Ready)
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                firebaseAuthRepo.signIn(email, password)
            }.onSuccess {result ->
                Log.i(TAG, "login: success with result $result")
                _uiState.emit(LoginUIState.LoginSuccessful)
            }.onFailure { throwable ->
                Log.e(TAG, "login: failed", throwable)
            }
        }
    }

    // TODO: for this mechanis, this is probably redundant
    fun signup(
        email: String,
        password: String
    ){
        viewModelScope.launch {
            kotlin.runCatching {
                firebaseAuthRepo.signUp(email, password)
            }.onSuccess { result ->
                Log.i(TAG, "signup: success with result $result")
            }.onFailure { throwable ->
                Log.e(TAG, "signup: failed", throwable)
            }
        }
    }

    private companion object{
        const val TAG = "LoginViewModel"
    }
}