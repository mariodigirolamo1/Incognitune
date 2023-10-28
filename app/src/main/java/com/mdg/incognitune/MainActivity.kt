package com.mdg.incognitune

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mdg.incognitune.ui.IncognituneApp
import com.mdg.incognitune.ui.theme.IncognituneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IncognituneTheme {
                IncognituneApp()
            }
        }
    }
}