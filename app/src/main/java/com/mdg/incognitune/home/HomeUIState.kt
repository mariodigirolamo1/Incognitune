package com.mdg.incognitune.home

sealed interface HomeUIState {
    object Loading: HomeUIState
    class Ready(val songLink: String) : HomeUIState
}