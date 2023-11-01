package com.mdg.incognitune.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mdg.incognitune.common.ui.theme.Typography

// TODO: Move this viewModel and navController away for preview
@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState = viewModel.uiState.collectAsState()
    when(uiState.value){
        is HomeUIState.Loading -> {
            Surface {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 12.dp,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
        is HomeUIState.Ready -> {
            HomeReady(
                viewModel = viewModel,
                // TODO: defer this read
                songLink = (uiState.value as HomeUIState.Ready).songLink
            )
        }
        is HomeUIState.UserNotSignedIn -> {
            navController.navigate("login")
        }
    }
}

@Composable
fun HomeReady(
    viewModel: HomeViewModel,
    songLink: String
) {
    val fillSizeModifier = Modifier.fillMaxSize()
    Surface(
        modifier = fillSizeModifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = fillSizeModifier
                .padding(14.dp)
        ) {
            val cardModifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
            Spacer(modifier = Modifier.size(28.dp))
            DailySuggestedSongCard(
                getSongLink = { songLink },
                modifier = cardModifier
            )
            Spacer(modifier = Modifier.size(14.dp))
            YourDailySubmissionCard(
                onSubmit = { songLink -> viewModel.addSong(songLink) },
                modifier = cardModifier
            )
            Spacer(modifier = Modifier.size(28.dp))
            SignOutButton(signOut = { viewModel.signOut() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailySuggestedSongCard(
    getSongLink: () -> String,
    modifier: Modifier = Modifier
) {
    val songLink = getSongLink()
    val uriHandler = LocalUriHandler.current
    Card(
        onClick = { uriHandler.openUri(songLink) }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = "Click me for your daily song! \uD83C\uDF81",
                style = Typography.titleLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourDailySubmissionCard(
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card{
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = "Paste your song link down there!",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(14.dp))
            // TODO: this belong to a viewModel
            var yourSongLink by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = yourSongLink,
                onValueChange = { yourSongLink = it },
                label = { Text("Your song link") }
            )
            Spacer(modifier = Modifier.size(14.dp))
            Button(onClick = { onSubmit(yourSongLink) }) {
                Text(text = "Send to the World!")
            }
        }
    }
}

@Composable
fun SignOutButton(
    signOut: () -> Unit
) {
    Button(onClick = signOut) {
        Text(text = "Logout")
    }
}