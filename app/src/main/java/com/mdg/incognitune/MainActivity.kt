package com.mdg.incognitune

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mdg.incognitune.common.ui.theme.IncognituneTheme
import com.mdg.incognitune.firebaseauth.data.FirebaseAuthRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuthRepo: FirebaseAuthRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // https://firebase.google.com/docs/auth/android/start?hl=en#check_current_auth_state
        firebaseAuthRepo.initAuth()

        setContent {
            IncognituneTheme(darkTheme = false) {
                IncognituneApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // TODO: understand if this actually needs to be passed down in composables
        if(firebaseAuthRepo.signedIn){
            // TODO: do somethign with user signed in?
        }else{
            // TODO: do something with user not signed in?
        }
    }
}