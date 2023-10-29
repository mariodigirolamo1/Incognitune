package com.mdg.incognitune.firebaseauth.domain

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepo {
    private lateinit var auth: FirebaseAuth

    fun initAuth(){
        auth = Firebase.auth
    }

    fun isUserSignedIn() = auth.currentUser == null

    /**
     * Usage
     * Tries to signup the user.
     *
     * Input
     * email: user email for future login
     * password: user password to login with given email
     *
     * Output
     * Success: return result with FirebaseUser signed in
     * Failure: return result with occured Exception
     */
    suspend fun signUp(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return kotlin.runCatching {
            suspendCoroutine {continuation ->
                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).addOnSuccessListener { authResult ->
                    Log.i(TAG, "signUp: some result in $authResult")
                    continuation.resume(authResult.user!!)
                }.addOnFailureListener{exception ->
                    Log.e(TAG, "signUp: failure", exception)
                    continuation.resumeWithException(exception)
                }
            }
        }
    }

    private companion object{
        const val TAG = "FirebaseAuthRepo"
    }
}