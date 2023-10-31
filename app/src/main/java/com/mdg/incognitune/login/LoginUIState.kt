package com.mdg.incognitune.login

sealed interface LoginUIState {
    object Loading: LoginUIState
    object Ready: LoginUIState
    object LoginSuccessful: LoginUIState
    class Error(message: String): LoginUIState
}