package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(navController: NavController) {
    var serviceDescription by remember { mutableStateOf("") }
    var clientName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Criar/Editar Ordem de Serviço") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = clientName,
                onValueChange = { clientName = it },
                label = { Text("Nome do Cliente") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = serviceDescription,
                onValueChange = { serviceDescription = it },
                label = { Text("Descrição do Serviço") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Aqui seria a lógica para salvar a ordem de serviço
                navController.popBackStack()
            }) {
                Text("Salvar Ordem de Serviço")
            }
        }
    }
}

@Preview
@Composable
fun PreviewCreateOrderScreen() {
    // Preview sem NavController
}