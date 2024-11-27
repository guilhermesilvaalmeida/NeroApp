package com.example.neroapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Data class to represent a service
data class Service(var title: String, var description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizarServicosScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<Service?>(null) }

    // Defina a lista de serviços diretamente aqui
    val services = listOf(
        Service("Desenvolvimento Mobile Aprovado", "A partir de R$ 2.000"),
        Service("Desenvolvimento Web Pendente", "A partir de R$ 1.500"),
        Service("Desenvolvimento de Software Aprovado", "A partir de R$ 5.000")
    )

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

            // Categorize services if needed (e.g., based on some condition like status)
            val approvedServices = services.filter { it.title.contains("Aprovado") }
            val pendingServices = services.filter { !it.title.contains("Aprovado") }

            Text("Serviços Aprovados", style = MaterialTheme.typography.headlineMedium)
            approvedServices.forEach { service ->
                ServiceCard(service) {
                    selectedService = service
                    showDialog = true
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Serviços Pendentes", style = MaterialTheme.typography.headlineMedium)
            pendingServices.forEach { service ->
                ServiceCard(service) {
                    selectedService = service
                    showDialog = true
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer text
            Text(
                text = "Desenvolvido by: Enzo e Guilherme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (showDialog && selectedService != null) {
            EditServiceDialog(
                service = selectedService!!,
                onDismiss = { showDialog = false },
                onUpdate = { updatedService ->
                    // Update the service in your list
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun ServiceCard(service: Service, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onEdit() },
        border = BorderStroke(1.dp, Color(0xFF6200EE)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(service.title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text(service.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Composable
fun EditServiceDialog(service: Service, onDismiss: () -> Unit, onUpdate: (Service) -> Unit) {
    var title by remember { mutableStateOf(service.title) }
    var description by remember { mutableStateOf(service.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Serviço") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    service.title = title
                    service.description = description
                    onUpdate(service)
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewVisualizarServicosScreen() {
    val fakeNavController = rememberNavController()
    VisualizarServicosScreen(navController = fakeNavController)
}
