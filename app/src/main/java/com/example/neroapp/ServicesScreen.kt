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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen() {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    var services by remember { mutableStateOf<List<OfferedService>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf(0) }
    var hours by remember { mutableStateOf("") }

    // Fetch services from Firestore
    LaunchedEffect(Unit) {
        if (currentUser != null) {
            val servicesSnapshot = db.collection("servicos")
                .whereEqualTo("userId", currentUser.uid) // Filtra os serviços pelo ID do usuário
                .get()
                .await()

            services = servicesSnapshot.documents.mapNotNull { document ->
                document.toObject(OfferedService::class.java)?.apply {
                    id = document.id
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Serviços Oferecidos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Mostrar serviços filtrados
            services.forEach { service ->
                ServiceItem(
                    serviceName = service.title,
                    price = "A partir de R$ ${service.price}",
                    hourlyRate = service.hourlyRate,
                    onRequestQuote = {
                        selectedService = service.title
                        hourlyRate = service.hourlyRate
                        showDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Desenvolvido by: Enzo e Guilherme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Exibe o Dialog de Solicitação de Orçamento
        if (showDialog) {
            RequestQuoteDialog(
                serviceName = selectedService,
                hourlyRate = hourlyRate,
                onDismiss = { showDialog = false },
                onRequestQuote = { requestedHours ->
                    // Envia o pedido de orçamento para o Firestore
                    if (currentUser != null) {
                        val quoteData = mapOf(
                            "serviceName" to selectedService,
                            "userId" to currentUser.uid,
                            "hourlyRate" to hourlyRate,
                            "hours" to requestedHours,
                            "totalPrice" to (requestedHours.toIntOrNull() ?: 0) * hourlyRate,
                            "status" to "Pendente"
                        )
                        db.collection("orcamentos").add(quoteData)
                            .addOnSuccessListener {
                                showDialog = false
                                // Aqui você pode adicionar feedback visual ou mensagem de sucesso
                            }
                            .addOnFailureListener {
                                // Lidar com falha ao salvar o orçamento
                            }
                    }
                }
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
fun RequestQuoteDialog(serviceName: String, hourlyRate: Int, onDismiss: () -> Unit, onRequestQuote: (String) -> Unit) {
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
            TextButton(onClick = {
                onRequestQuote(hours)
            }) {
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

data class OfferedService(
    val title: String = "",
    val price: String = "",
    val hourlyRate: Int = 0,
    var id: String = ""
)
