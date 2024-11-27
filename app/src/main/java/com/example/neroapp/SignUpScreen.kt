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
    var isCompany by remember { mutableStateOf(false) }  // Estado para determinar o tipo de cadastro

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

            // Campo para Nome
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

            // Campo para Email
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

            // Campo para Senha
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

            // Campo para Data de Nascimento
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

            // Campo para Telefone
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

            // Escolha entre Cliente ou Empresa
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

            // Botão para cadastrar
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            // Criação do usuário no Firebase Authentication
                            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                            // Adiciona os dados adicionais ao Firestore
                            val user = authResult.user
                            user?.let {
                                val userData = hashMapOf(
                                    "nome" to name,
                                    "telefone" to phone,
                                    "dataNascimento" to birthDate,
                                    "email" to email
                                )

                                // Inserção dos dados na coleção adequada (clientes ou empresas)
                                if (isCompany) {
                                    db.collection("empresas").document(it.uid)
                                        .set(userData)
                                        .await()
                                } else {
                                    db.collection("clientes").document(it.uid)
                                        .set(userData)
                                        .await()
                                }

                                // Navegação para a tela de login ou página principal
                                onNavigateToSignIn() // Navega para a tela de login
                            }

                        } catch (e: Exception) {
                            errorMessage = "Erro ao cadastrar: ${e.message}"
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

            // Exibe a mensagem de erro caso haja
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
