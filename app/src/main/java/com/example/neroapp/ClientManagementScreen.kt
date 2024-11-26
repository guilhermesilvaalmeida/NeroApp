package com.example.neroapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

// Definição da classe Cliente
data class Cliente(
    val id: String,
    val nome: String,
    val email: String,
    val telefone: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientManagementScreen(navController: NavController) {
    // Cor principal: Roxo
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    // Lista de clientes simulados
    val clientes = remember { mutableStateListOf(
        Cliente("001", "João Silva", "joao@gmail.com", "(11) 91234-5678"),
        Cliente("002", "Maria Souza", "maria@gmail.com", "(11) 98765-4321"),
        Cliente("003", "Carlos Lima", "carlos@gmail.com", "(11) 91234-1234"),
        Cliente("004", "Ana Pereira", "ana@gmail.com", "(11) 98765-5678")
    ) }

    var searchQuery by remember { mutableStateOf("") }

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
                items(clientes.size) { index ->
                    val cliente = clientes[index]

                    if (cliente.nome.contains(searchQuery, ignoreCase = true)) {
                        ClientItem(cliente)
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
fun ClientItem(cliente: Cliente) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Navegar para detalhes ou editar */ },
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

            // Botões de ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Lógica para editar cliente
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Editar", color = Color.White)
                }

                Button(
                    onClick = {
                        // Lógica para excluir cliente
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Excluir", color = Color.White)
                }
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
