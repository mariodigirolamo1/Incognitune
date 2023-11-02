package com.mdg.incognitune.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {
    when(val uiState = viewModel.uiState.collectAsState().value){
        LoginUIState.Loading -> {
            CircularProgressIndicator()
        }
        is LoginUIState.Ready -> {
            LoginCard(
                signup = {email, password ->
                    viewModel.signup(email,password)
                },
                login = { email, password ->
                    viewModel.login(email,password, navigateToHome)
                },
                navigateToHome = navigateToHome,
                errorMessage = uiState.errorMessage
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginCard(
    signup: (String, String) -> Unit,
    login: (String, String) -> Unit,
    navigateToHome: () -> Unit,
    errorMessage: String?
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            Card{
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(all = 20.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {email = it},
                        label = {
                            Text(text = "E-Mail")
                        },
                        isError = errorMessage != null
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {password = it},
                        label = {
                            Text(text = "Password")
                        },
                        isError = errorMessage != null
                    )
                    errorMessage?.let{
                        Text(text = errorMessage)
                    }
                    Button(
                        onClick = { signup(email, password) }
                    ) {
                        Text("Sign Up")
                    }
                    Button(
                        onClick = {
                            login(email, password)
                        }
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}