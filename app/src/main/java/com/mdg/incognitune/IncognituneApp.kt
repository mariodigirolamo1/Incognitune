package com.mdg.incognitune

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mdg.incognitune.home.Home
import com.mdg.incognitune.login.Login

@Composable
fun IncognituneApp() {
    val navController = rememberNavController()
    Surface {
        NavHost(navController = navController, startDestination = "home"){
            composable("home"){ Home(navController = navController) }
            composable("login"){
                Login(
                    navigateToHome = { navController.navigate("home") }
                )
            }
        }
    }
}