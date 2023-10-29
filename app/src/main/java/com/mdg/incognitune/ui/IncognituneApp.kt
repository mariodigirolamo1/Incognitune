package com.mdg.incognitune.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mdg.incognitune.ui.theme.IncognituneTheme
import com.mdg.incognitune.ui.theme.Typography

@Composable
fun IncognituneApp() {
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
            DailySuggestedSongCard(modifier = cardModifier)
            Spacer(modifier = Modifier.size(14.dp))
            YourDailySubmissionCard(modifier = cardModifier)
        }
    }
}

@Composable
fun DailySuggestedSongCard(
    modifier: Modifier = Modifier
) {
    Card{
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = "Suggested song",
                style = Typography.titleLarge
            )
            Spacer(modifier = Modifier.size(14.dp))
            Text(
                text = "This will show the fantastical song link!",
                style = Typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourDailySubmissionCard(
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
            Button(onClick = {}) {
                Text(text = "Send to the World!")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IncognituneAppPreview() {
    IncognituneTheme {
        IncognituneApp()
    }
}