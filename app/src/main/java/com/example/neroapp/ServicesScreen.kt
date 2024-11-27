package com.example.neroapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(navController: NavController) {
    var services by remember { mutableStateOf<List<ServiceItemModel>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<ServiceItemModel?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        services = fetchServices()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Serviços") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            services.forEach { service ->
                ServiceItem(
                    serviceName = service.name,
                    description = service.description,
                    price = service.price,
                    onRequestQuote = {
                        selectedService = service
                        showDialog = true
                    }
                )
            }

            if (showSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { showSnackbar = false }) {
                            Text("OK", color = Color.White)
                        }
                    }
                ) {
                    Text(snackbarMessage)
                }
            }
        }
    }

    if (showDialog && selectedService != null && currentUser != null) {
        RequestQuoteDialog(
            serviceName = selectedService!!.name,
            empresaId = selectedService!!.userId, // Using userId from servicos as empresaId
            userId = currentUser.uid,
            userName = currentUser.displayName ?: "Usuário",
            onDismiss = { showDialog = false },
            onQuoteSent = { message ->
                snackbarMessage = message
                showSnackbar = true
                showDialog = false
            }
        )
    }
}

@Composable
fun ServiceItem(serviceName: String, description: String, price: Int, onRequestQuote: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = serviceName, fontSize = 18.sp)
            Text(text = description, fontSize = 14.sp, color = Color.Gray)
            Text(text = "Preço: R$ $price", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRequestQuote) {
                Text("Solicitar Orçamento")
            }
        }
    }
}

@Composable
fun RequestQuoteDialog(
    serviceName: String,
    empresaId: String,
    userId: String,
    userName: String,
    onDismiss: () -> Unit,
    onQuoteSent: (String) -> Unit
) {
    var customerMessage by remember { mutableStateOf("") }
    val isSubmitEnabled = customerMessage.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Solicitar Orçamento - $serviceName") },
        text = {
            TextField(
                value = customerMessage,
                onValueChange = { customerMessage = it },
                label = { Text("Descrição da solicitação") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    sendQuoteRequest(serviceName, customerMessage, empresaId, userId, userName) {
                        onQuoteSent("Orçamento enviado com sucesso!")
                    }
                },
                enabled = isSubmitEnabled
            ) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun sendQuoteRequest(
    serviceName: String,
    customerMessage: String,
    empresaId: String,
    userId: String,
    userName: String,
    onQuoteSent: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val request = hashMapOf(
        "serviceName" to serviceName,
        "customerMessage" to customerMessage,
        "empresaId" to empresaId, // Setting empresaId in the request
        "userId" to userId,
        "userName" to userName,
        "timestamp" to System.currentTimeMillis()
    )
    db.collection("orcamentos").add(request)
        .addOnSuccessListener { onQuoteSent() }
        .addOnFailureListener { println("Erro ao enviar orçamento.") }
}

suspend fun fetchServices(): List<ServiceItemModel> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("servicos").get().await()
        snapshot.documents.mapNotNull { doc ->
            ServiceItemModel(
                name = doc.getString("name") ?: "",
                description = doc.getString("description") ?: "",
                price = doc.getLong("price")?.toInt() ?: 0,
                userId = doc.getString("userId") ?: ""
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}

data class ServiceItemModel(
    val name: String,
    val description: String,
    val price: Int,
    val userId: String
)
