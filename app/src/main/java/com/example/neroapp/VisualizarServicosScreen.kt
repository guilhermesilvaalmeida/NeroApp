package com.example.neroapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

// Atualizando a classe de dados para incluir 'name' e 'price'
data class Service(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var name: String = "",
    var price: Double = 0.0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizarServicosScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var services by remember { mutableStateOf<List<Service>>(emptyList()) }
    var showConfirmation by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()

    // Carregar todos os serviços da coleção "servicos"
    LaunchedEffect(Unit) {
        db.collection("servicos")
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                services = result.documents.map { document ->
                    Service(
                        id = document.id,
                        title = document.getString("title") ?: "Sem título",
                        description = document.getString("description") ?: "Sem descrição",
                        name = document.getString("name") ?: "Sem nome",
                        price = document.getDouble("price") ?: 0.0
                    )
                }
            }
            .addOnFailureListener { exception ->
                println("Erro ao carregar dados: ${exception.message}")
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visualizar Serviços", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Serviços Cadastrados", style = MaterialTheme.typography.headlineLarge)

            services.forEach { service ->
                ServiceCard(service, onClick = {
                    selectedService = service
                    showDialog = true
                })

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Exibir os detalhes do serviço em um dialog
            if (showDialog && selectedService != null) {
                ServiceDetailsDialog(
                    service = selectedService!!,
                    onDismiss = { showDialog = false },
                    onEditClick = { service ->
                        selectedService = service
                        showConfirmation = true
                    }
                )
            }

            if (showConfirmation) {
                EditServiceDialog(
                    service = selectedService!!,
                    onDismiss = { showConfirmation = false },
                    onUpdate = { updatedService ->
                        updateServiceInDatabase(updatedService)
                        showConfirmation = false
                    }
                )
            }
        }
    }
}

@Composable
fun ServiceCard(service: Service, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, Color(0xFF6200EE)),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(service.title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(service.description)
            }

            // Ícone de edição
            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar Serviço"
                )
            }
        }
    }
}

@Composable
fun ServiceDetailsDialog(service: Service, onDismiss: () -> Unit, onEditClick: (Service) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalhes do Serviço") },
        text = {
            Column {
                Text("Título: ${service.title}")
                Text("Descrição: ${service.description}")
                Text("Nome: ${service.name}")
                Text("Preço: ${service.price}")
            }
        },
        confirmButton = {
            Button(onClick = { onEditClick(service) }) {
                Text("Editar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

@Composable
fun EditServiceDialog(service: Service, onDismiss: () -> Unit, onUpdate: (Service) -> Unit) {
    var description by remember { mutableStateOf(service.description) }
    var name by remember { mutableStateOf(service.name) }
    var price by remember { mutableStateOf(service.price.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Serviço") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Preço por Hora") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedService = service.copy(
                    description = description,
                    name = name,
                    price = price.toDoubleOrNull() ?: 0.0
                )
                onUpdate(updatedService)
            }) {
                Text("Atualizar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun updateServiceInDatabase(service: Service) {
    val db = FirebaseFirestore.getInstance()

    // Atualizar os dados do serviço no Firestore
    db.collection("servicos").document(service.id)
        .update(
            "description", service.description,
            "name", service.name,
            "price", service.price
        )
        .addOnSuccessListener {
            println("Serviço atualizado com sucesso!")
        }
        .addOnFailureListener { e ->
            println("Erro ao atualizar serviço: ${e.message}")
        }
}

@Preview
@Composable
fun PreviewVisualizarServicosScreen() {
    VisualizarServicosScreen(navController = rememberNavController())
}
