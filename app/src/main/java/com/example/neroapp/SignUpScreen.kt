package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignUpScreen(auth: FirebaseAuth, onNavigateToSignIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Criar Conta", style = MaterialTheme.typography.headlineLarge)

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val result = signUpWithEmailAndPasswordSuspend(auth, email, password)
                            errorMessage = result
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToSignIn) {
                    Text("Já tem uma conta? Faça login")
                }

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
}

suspend fun signUpWithEmailAndPasswordSuspend(
    auth: FirebaseAuth,
    email: String,
    password: String
): String? {
    return try {
        auth.createUserWithEmailAndPassword(email, password).await()
        null // No error message
    } catch (e: Exception) {
        e.localizedMessage
    }
}
