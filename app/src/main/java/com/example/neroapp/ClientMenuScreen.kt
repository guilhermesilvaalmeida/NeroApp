package com.example.neroapp

import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoneyOff // Ícone alternativo para orçamentos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientMenuScreen(navController: NavController, clientName: String) {
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu do Cliente", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text("Olá $clientName, o que vamos fazer hoje?", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(32.dp))

                // Opções do cliente
                Column {
                    MenuOption("Serviços Oferecidos", "Veja todos os serviços disponíveis.", Icons.Filled.Build) {
                        // Navegar para a tela de serviços oferecidos
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuOption("Ordens de Serviço", "Acompanhe o status de suas ordens.",
                        Icons.AutoMirrored.Filled.List
                    ) {
                        // Navegar para a tela de ordens de serviço
                    }
                    Spacer(modifier = Modifier.height(16.dp)) // Espaço entre opções
                    MenuOption("Status dos Orçamentos", "Verifique o status dos seus orçamentos.", Icons.Filled.MoneyOff) {
                        // Navegar para a tela de status dos orçamentos
                    }
                }
            }

            // Texto na parte inferior
            Text(
                text = "Desenvolvido by: Enzo e Guilherme",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MenuOption(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        border = BorderStroke(2.dp, Color(0xFF6200EE)), // Borda roxa
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF6200EE), modifier = Modifier.size(32.dp)) // Ícone maior
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)) // Texto maior
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), color = Color.Gray) // Descrição
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClientMenuScreen() {
    val fakeNavController = rememberNavController()
    ClientMenuScreen(navController = fakeNavController, clientName = "Cliente XYZ")
}
