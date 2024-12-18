package com.example.neroapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    auth: FirebaseAuth,
    onNavigateToSignUp: () -> Unit,
    onNavigateToClientHome: () -> Unit, // Mantido apenas a navegação para cliente
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var rememberMe by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val image = painterResource(id = R.drawable.nero)
            Image(painter = image, contentDescription = null, modifier = Modifier.size(200.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bem-vindo de volta!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryColor,
                    cursorColor = primaryColor,
                    unfocusedIndicatorColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de senha com ícone para visualizar
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryColor,
                    cursorColor = primaryColor,
                    unfocusedIndicatorColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(icon, contentDescription = "Toggle password visibility")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // Botão de login
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onNavigateToClientHome()
                            } else {
                                errorMessage = task.exception?.message ?: "Erro ao fazer login"
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Entrar", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensagem de erro
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de "Esqueci minha senha"
            TextButton(onClick = { navController.navigate("recoverPassword") }) {
                Text("Esqueci minha senha", color = primaryColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de "Cadastrar-se"
            TextButton(onClick = { onNavigateToSignUp() }) {
                Text("Não tem uma conta? Cadastre-se", color = primaryColor)
            }
        }
    }
}
