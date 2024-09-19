package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Relatórios de Ordens de Serviço") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Relatórios de ordens de serviço serão exibidos aqui.")
            // Adicione componentes gráficos para os relatórios
        }
    }
}

@Preview
@Composable
fun PreviewReportsScreen() {
    // Preview sem NavController
}