package com.mdg.incognitune

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mdg.incognitune.home.Home
import com.mdg.incognitune.login.Login

@Composable
fun IncognituneApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){
        composable("home"){ Home() }
        composable("login"){ Login() }
    }
}