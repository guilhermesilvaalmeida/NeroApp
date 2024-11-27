package com.example.neroapp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentQuotesScreen(navController: NavController) {
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    // Simulação de dados de orçamentos enviados
    val quotes = listOf(
        Quote("Desenvolvimento Mobile", "Cliente A", "R$ 2.500", "2024-09-24"),
        Quote("Desenvolvimento Web", "Cliente B", "R$ 1.800", "2024-09-23"),
        Quote("Desenvolvimento de Software", "Cliente C", "R$ 5.500", "2024-09-22")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orçamentos Enviados", color = Color.White) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Lista de Orçamentos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            // Exibir os orçamentos
            for (quote in quotes) {
                QuoteItem(quote = quote)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Desenvolvido by: Enzo e Guilherme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun QuoteItem(quote: Quote) {
    var showCancelDialog by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp)
            .border(2.dp, Color(0xFF6200EE)), // Adiciona borda roxa
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Serviço: ${quote.serviceName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Cliente: ${quote.clientName}", fontSize = 16.sp)
            Text(text = "Preço: ${quote.price}", fontSize = 16.sp)
            Text(text = "Data: ${quote.date}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /* Lógica para aprovar orçamento */ }) {
                    Text("Aprovar")
                }
                Button(onClick = { showCancelDialog = true }) {
                    Text("Cancelar")
                }
            }
        }
    }

    // Diálogo para cancelar orçamento
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Motivo do Cancelamento") },
            text = {
                Column {
                    Text("Insira o motivo do cancelamento:")
                    TextField(
                        value = cancelReason,
                        onValueChange = { cancelReason = it },
                        placeholder = { Text("Motivo") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Lógica para enviar o motivo do cancelamento
                        showCancelDialog = false
                        cancelReason = "" // Limpa o motivo após o envio
                    }
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                Button(onClick = { showCancelDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

data class Quote(
    val serviceName: String,
    val clientName: String,
    val price: String,
    val date: String
)

@Preview(showBackground = true)
@Composable
fun PreviewSentQuotesScreen() {
    val fakeNavController = rememberNavController()
    SentQuotesScreen(navController = fakeNavController)
}
