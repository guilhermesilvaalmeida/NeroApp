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
fun ManageCompaniesScreen(navController: NavController) {
    var companyName by remember { mutableStateOf("") }
    var clientName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gerenciar Empresas e Clientes") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                label = { Text("Nome da Empresa") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = clientName,
                onValueChange = { clientName = it },
                label = { Text("Nome do Cliente") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Lógica para salvar empresa/cliente
                navController.popBackStack()
            }) {
                Text("Salvar")
            }
        }
    }
}

@Preview
@Composable
fun PreviewManageCompaniesScreen() {
    // Preview sem NavController
}