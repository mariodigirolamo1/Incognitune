package com.mdg.incognitune.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.mdg.incognitune.firebaseauth.data.FirebaseAuthRepo
import com.mdg.incognitune.firebaseauth.model.UserSignedInState
import com.mdg.incognitune.home.HomeViewModel
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
        viewModelScope.launch {
            _uiState.emit(LoginUIState.Ready())
        }
    }

    fun login(
        email: String,
        password: String,
        navigateToHome: () -> Unit
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                firebaseAuthRepo.signIn(email, password)
            }.onSuccess {result ->
                Log.i(TAG, "login: success with result $result")
                if(result.isSuccess){
                    navigateToHome()
                }else{
                    val exception = result.exceptionOrNull()
                    exception?.let{
                        when(exception){
                            is FirebaseException -> {
                                if(exception.localizedMessage?.contains(
                                        other = "INVALID_LOGIN_CREDENTIALS",
                                        ignoreCase = true) == true) {
                                    _uiState.emit(LoginUIState.Ready("Wrong credentials"))
                                }else {
                                    _uiState.emit(LoginUIState.Ready(result.exceptionOrNull()?.localizedMessage))
                                }
                            }
                            else -> {
                                _uiState.emit(LoginUIState.Ready(result.exceptionOrNull()?.localizedMessage))
                            }
                        }
                    }
                }
            }.onFailure { throwable ->
                Log.e(TAG, "login: failed", throwable)
            }
        }
    }

    // TODO: for this mechanism, this is probably redundant
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