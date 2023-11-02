package com.mdg.incognitune.login

sealed interface LoginUIState {
    object Loading: LoginUIState
    class Ready(val errorMessage: String? = null): LoginUIState
}