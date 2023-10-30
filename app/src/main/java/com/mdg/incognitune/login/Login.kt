package com.mdg.incognitune.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp

@Composable
fun Login() {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            LoginCard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginCard() {
    var text by rememberSaveable { mutableStateOf("") }

    Card{
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(all = 20.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                label = {
                    Text(text = "Mail")
                }
            )
            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                label = {
                    Text(text = "Password")
                }
            )
            Button(
                onClick = {}
            ) {
                Text("Login")
            }
        }
    }
}