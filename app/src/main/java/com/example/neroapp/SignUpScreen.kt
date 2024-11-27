package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(auth: FirebaseAuth, onNavigateToSignIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isCompany by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crie sua conta",
                style = MaterialTheme.typography.headlineLarge,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campos de entrada
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryColor,
                    cursorColor = primaryColor,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = primaryColor,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryColor,
                    cursorColor = primaryColor,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = primaryColor,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryColor,
                    cursorColor = primaryColor,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = primaryColor,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Data de Nascimento") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryColor,
                    cursorColor = primaryColor,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = primaryColor,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryColor,
                    cursorColor = primaryColor,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = primaryColor,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !isCompany,
                    onClick = { isCompany = false }
                )
                Text("Cliente", modifier = Modifier.padding(start = 8.dp))

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = isCompany,
                    onClick = { isCompany = true }
                )
                Text("Empresa", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            // Criação do usuário
                            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                            // Dados no Firestore
                            val user = authResult.user
                            user?.let {
                                val userData = hashMapOf(
                                    "nome" to name,
                                    "telefone" to phone,
                                    "dataNascimento" to birthDate,
                                    "email" to email
                                )

                                if (isCompany) {
                                    db.collection("empresas").document(it.uid)
                                        .set(userData).await()
                                } else {
                                    db.collection("clientes").document(it.uid)
                                        .set(userData).await()
                                }

                                // Mensagem de sucesso
                                successMessage = "Usuário cadastrado com sucesso!"
                                errorMessage = null

                                // Limpar os campos
                                email = ""
                                password = ""
                                name = ""
                                birthDate = ""
                                phone = ""
                            }

                        } catch (e: Exception) {
                            errorMessage = if (e.message?.contains("The email address is already in use") == true) {
                                "Este email já está em uso. Por favor, use outro."
                            } else {
                                "Erro ao cadastrar: ${e.message}"
                            }
                            successMessage = null
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Cadastrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToSignIn) {
                Text("Já tem uma conta? Faça login", color = primaryColor)
            }

            successMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = Color(0xFF00C853), // Cor de sucesso (verde)
                    style = MaterialTheme.typography.bodyLarge
                )
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
}
