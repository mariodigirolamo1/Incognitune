package com.mdg.incognitune.home

sealed interface HomeUIState {
    object Loading: HomeUIState
    object UserNotSignedIn: HomeUIState
    class Ready(
        var songLink: String,
        var hasSentDailySong: Boolean
    ) : HomeUIState
}