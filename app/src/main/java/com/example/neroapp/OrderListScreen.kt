package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ordens de Serviço") })
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            // Exemplo de lista de ordens de serviço
            items(10) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = { navController.navigate("orderDetails/$index") }
                ) {
                    Text(text = "Ordem de Serviço #$index", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOrderListScreen() {
    // Preview sem NavController
}
