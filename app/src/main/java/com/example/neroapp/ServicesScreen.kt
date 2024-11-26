package com.example.neroapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen() {
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Serviços Oferecidos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            ServiceItem(
                serviceName = "Desenvolvimento Mobile",
                price = "A partir de R$ 2.000",
                hourlyRate = 100,
                onRequestQuote = { selectedService = "Desenvolvimento Mobile"; showDialog = true }
            )
            ServiceItem(
                serviceName = "Desenvolvimento Web",
                price = "A partir de R$ 1.500",
                hourlyRate = 80,
                onRequestQuote = { selectedService = "Desenvolvimento Web"; showDialog = true }
            )
            ServiceItem(
                serviceName = "Desenvolvimento de Software",
                price = "A partir de R$ 5.000",
                hourlyRate = 120,
                onRequestQuote = { selectedService = "Desenvolvimento de Software"; showDialog = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Desenvolvido by: Enzo e Guilherme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (showDialog) {
            RequestQuoteDialog(
                serviceName = selectedService,
                hourlyRate = when (selectedService) {
                    "Desenvolvimento Mobile" -> 100
                    "Desenvolvimento Web" -> 80
                    "Desenvolvimento de Software" -> 120
                    else -> 0
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun ServiceItem(serviceName: String, price: String, hourlyRate: Int, onRequestQuote: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = serviceName,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = price,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onRequestQuote,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Solicitar Orçamento", color = Color.White)
            }
        }
    }
}

@Composable
fun RequestQuoteDialog(serviceName: String, hourlyRate: Int, onDismiss: () -> Unit) {
    var hours by remember { mutableStateOf("") }
    val totalPrice = (hours.toIntOrNull() ?: 0) * hourlyRate

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Solicitar Orçamento - $serviceName") },
        text = {
            Column {
                Text("Valor por hora: R$ $hourlyRate")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = hours,
                    onValueChange = { hours = it },
                    label = { Text("Horas necessárias") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Preço total: R$ $totalPrice", fontSize = 16.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Enviar Orçamento")
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
fun PreviewServicesScreen() {
    ServicesScreen()
}
