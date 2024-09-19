package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(navController: NavController, orderId: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Ordem #$orderId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                         Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Informações detalhadas da ordem de serviço #$orderId")
            // Adicione mais detalhes relevantes aqui
        }
    }
}

@Preview
@Composable
fun PreviewOrderDetailsScreen() {
    // Use um NavController falso para preview
    val fakeNavController = NavController(LocalContext.current)
    OrderDetailsScreen(orderId = "1", navController = fakeNavController)
}
