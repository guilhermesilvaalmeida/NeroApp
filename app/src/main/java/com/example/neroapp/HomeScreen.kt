package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tela Inicial") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("orderList") }) {
                Text("Ordens de Serviço")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("settings") }) {
                Text("Configurações")
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    // Preview sem NavController
}