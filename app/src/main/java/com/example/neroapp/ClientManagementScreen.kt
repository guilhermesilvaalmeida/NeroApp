package com.example.neroapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateListOf

// Definição da classe Cliente
data class Cliente(
    val id: String,
    val nome: String,
    val email: String,
    val telefone: String
)

fun fetchClientes(clientesList: MutableList<Cliente>) {
    val db = FirebaseFirestore.getInstance() // Instância do Firestore

    db.collection("clientes")
        .get()
        .addOnSuccessListener { result ->
            println("Dados recebidos: ${result.documents.size} documentos.")
            clientesList.clear() // Limpa a lista antes de adicionar novos itens
            for (document in result) {
                val cliente = Cliente(
                    id = document.id,
                    nome = document.getString("nome") ?: "",
                    email = document.getString("email") ?: "",
                    telefone = document.getString("telefone") ?: ""
                )
                clientesList.add(cliente)
            }
        }
        .addOnFailureListener { e ->
            println("Erro ao buscar dados: $e")
        }
}

fun deleteCliente(cliente: Cliente, clientesList: MutableList<Cliente>) {
    val db = FirebaseFirestore.getInstance()
    db.collection("clientes")
        .document(cliente.id)
        .delete()
        .addOnSuccessListener {
            println("Cliente ${cliente.nome} excluído com sucesso")
            fetchClientes(clientesList)
        }
        .addOnFailureListener { e ->
            println("Erro ao excluir cliente: $e")
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientManagementScreen(navController: NavController) {
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    // Lista de clientes que será preenchida
    val clientes = remember { mutableStateListOf<Cliente>() }
    var searchQuery by remember { mutableStateOf("") }

    // Carregar os dados do Firestore ao iniciar a tela
    LaunchedEffect(Unit) {
        fetchClientes(clientes)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerenciamento de Clientes", color = Color.White) },
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
            // Barra de Pesquisa
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Pesquisar Cliente") },
                    modifier = Modifier
                        .weight(1f)
                        .border(BorderStroke(1.dp, primaryColor), shape = RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = primaryColor
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Clientes filtrados
            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(clientes) { cliente ->
                    if (cliente.nome.contains(searchQuery, ignoreCase = true)) {
                        ClientItem(
                            cliente,
                            onDelete = { deleteCliente(it, clientes) }
                        )
                    }
                }
            }

            // Texto de desenvolvimento
            Text(
                text = "Desenvolvido por: Enzo e Guilherme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ClientItem(cliente: Cliente, onDelete: (Cliente) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        border = BorderStroke(2.dp, Color(0xFF6200EE)),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "ID: ${cliente.id}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = cliente.nome,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(text = "Email: ${cliente.email}")
            Text(text = "Telefone: ${cliente.telefone}")

            // Botão de exclusão
            Button(
                onClick = { onDelete(cliente) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Excluir", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClientManagementScreen() {
    val fakeNavController = rememberNavController()
    ClientManagementScreen(navController = fakeNavController)
}