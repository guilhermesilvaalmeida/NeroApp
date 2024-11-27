package com.example.neroapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Data class to represent a service
data class Service(var title: String, var description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizarServicosScreen(navController: NavController, services: List<Service>) {
    var serviceList by remember { mutableStateOf(services.toMutableList()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<Service?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visualizar Serviços", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Serviços Cadastrados:", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de serviços
            if (serviceList.isEmpty()) {
                Text("Nenhum serviço cadastrado.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            } else {
                LazyColumn {
                    items(serviceList) { service ->
                        ServiceCard(service) {
                            // Abre o diálogo para editar o serviço
                            selectedService = service
                            showDialog = true
                        }
                        Spacer(modifier = Modifier.height(16.dp)) // Espaço entre os cartões
                    }
                }
            }
        }

        // Dialog para editar o serviço
        if (showDialog && selectedService != null) {
            EditServiceDialog(
                service = selectedService!!,
                onDismiss = { showDialog = false },
                onUpdate = { updatedService ->
                    serviceList[serviceList.indexOf(selectedService)] = updatedService
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
            .padding(8.dp),
        border = BorderStroke(2.dp, Color(0xFF6200EE)), // Borda roxa
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(service.title, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(service.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            // Ícone de lápis para editar
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar Serviço",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onEdit() }, // Chama a função de edição ao clicar
                tint = Color(0xFF6200EE) // Cor do ícone
            )
        }
    }
}

@Composable
fun EditServiceDialog(service: Service, onDismiss: () -> Unit, onUpdate: (Service) -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue(service.title)) }
    var description by remember { mutableStateOf(TextFieldValue(service.description)) }

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
                    service.title = title.text
                    service.description = description.text
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

    // Adicionando os serviços de exemplo
    val sampleServices = listOf(
        Service("Desenvolvimento Mobile", "A partir de R$ 2.000"),
        Service("Desenvolvimento Web", "A partir de R$ 1.500"),
        Service("Desenvolvimento de Software", "A partir de R$ 5.000")
    )
    VisualizarServicosScreen(navController = fakeNavController, services = sampleServices)
}