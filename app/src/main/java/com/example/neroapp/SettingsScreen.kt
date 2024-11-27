package com.example.neroapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.TextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    // Cor principal: Roxo
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White
    val context = navController.context
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Estado para controlar o popup de alteração de nome
    var showNameDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var userCollection by remember { mutableStateOf("") } // Variável para armazenar a coleção do usuário

    // Função para verificar em qual coleção o usuário está (clientes ou empresas)
    fun getUserCollection(userId: String, onResult: (String) -> Unit) {
        db.collection("clientes").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onResult("clientes")
                } else {
                    // Se não for encontrado na coleção "clientes", verifica na coleção "empresas"
                    db.collection("empresas").document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                onResult("empresas")
                            } else {
                                onResult("") // Caso não seja encontrado em nenhuma coleção
                            }
                        }
                        .addOnFailureListener {
                            onResult("") // Caso ocorra algum erro
                        }
                }
            }
            .addOnFailureListener {
                onResult("") // Caso ocorra algum erro
            }
    }

    // Função para alterar o nome do usuário
    fun updateUserName() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            // Chama a função para verificar a coleção do usuário
            getUserCollection(userId) { collection ->
                if (collection.isNotEmpty()) {
                    // Alterar nome no Firestore
                    db.collection(collection).document(userId)
                        .update("nome", newName)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Nome alterado com sucesso", Toast.LENGTH_SHORT).show()
                            showNameDialog = false
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro ao alterar nome", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Usuário não encontrado em nenhuma coleção", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Configurações da Conta",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Opções de configuração da conta
            ConfigOptionItem(title = "Alterar Nome de Usuário", onClick = {
                showNameDialog = true
            })

            // Removido o botão de Alterar Senha

            ConfigOptionItem(title = "Excluir Conta", onClick = {
                // Desconectar o usuário
                auth.signOut()
                FirebaseAuth.getInstance().signOut()
                navController.navigate("signIn")
                Toast.makeText(context, "Conta excluída", Toast.LENGTH_SHORT).show()

            })

            Spacer(modifier = Modifier.height(24.dp))

            // Configurações de Notificações (Desativadas)
            Text(
                text = "Configurações de Notificações",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botões desativados
            ConfigOptionItem(title = "Notificações Push", onClick = {}, enabled = false)
            ConfigOptionItem(title = "Notificações por E-mail", onClick = {}, enabled = false)

            Spacer(modifier = Modifier.height(24.dp))

            // Configurações de Tema (Desativado)
            Text(
                text = "Configurações de Tema",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botão de Modo Claro Desativado
            ConfigOptionItem(title = "Modo Claro", onClick = {}, enabled = false)

            Spacer(modifier = Modifier.height(16.dp))

            // Texto final de desenvolvimento
            Text(
                text = "Desenvolvido por: Enzo e Guilherme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Popup para alterar nome de usuário
        if (showNameDialog) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = { Text("Alterar Nome de Usuário") },
                text = {
                    Column {
                        TextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Novo Nome") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        updateUserName()
                    }) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNameDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun ConfigOptionItem(title: String, onClick: () -> Unit, enabled: Boolean = true) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = enabled) { onClick() },
        border = BorderStroke(1.dp, Color(0xFF6200EE)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Opção de Configuração",
                tint = Color(0xFF6200EE)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    val fakeNavController = rememberNavController()
    SettingsScreen(navController = fakeNavController)
}
