package com.mdg.incognitune.firebaseauth.data

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mdg.incognitune.firebaseauth.model.UserSignedInState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.sign

class FirebaseAuthRepo {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    var signedIn = false

    fun initAuth(){
        auth = Firebase.auth
        user = auth.currentUser
    }

    fun isUserSignedIn() = auth.currentUser != null

    fun getUserId(): String? = auth.currentUser?.email

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

    suspend fun signIn(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return kotlin.runCatching {
            suspendCoroutine {continuation ->
                auth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener {authResult ->
                        Log.i(TAG, "signIn: some result in $authResult")
                        user = authResult.user
                        continuation.resume(authResult.user!!)
                    }
                    .addOnFailureListener{exception ->
                        Log.e(TAG, "signIn: failure", exception)
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }

    suspend fun signOut(){
        suspendCoroutine { continuation ->
            val authStateListener = AuthStateListener {
                if(it.currentUser == null && user != null){
                    user = null
                    continuation.resume(Unit)
                }
            }
            auth.addAuthStateListener(authStateListener)
            auth.signOut()
        }
    }

    private companion object{
        const val TAG = "FirebaseAuthRepo"
    }
}