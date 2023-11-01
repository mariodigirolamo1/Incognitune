package com.mdg.incognitune.firebaseauth.model

import com.google.firebase.auth.FirebaseUser

sealed interface UserSignedInState {
    object Unknown: UserSignedInState
    class LoggedIn(user: FirebaseUser): UserSignedInState
    object LoggedOut: UserSignedInState
}