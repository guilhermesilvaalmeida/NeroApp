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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(navController: NavController) {
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    // Lista de serviços carregados do Firestore
    var services by remember { mutableStateOf<List<ServiceItemModel>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<ServiceItemModel?>(null) }

    // Carrega os serviços do Firestore
    LaunchedEffect(Unit) {
        services = fetchServices()
    }

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
            // Exibe os serviços dinamicamente
            for (service in services) {
                ServiceItem(
                    serviceName = service.name,
                    description = service.description,
                    price = service.price,
                    clientName = service.clientName,  // Passa o clientName para o ServiceItem
                    onRequestQuote = {
                        selectedService = service
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

        // Diálogo de orçamento
        selectedService?.let { service ->
            if (showDialog) {
                RequestQuoteDialog(
                    serviceName = service.name,
                    price = service.price,
                    clientName = service.clientName,  // Passa o clientName para o RequestQuoteDialog
                    userId = service.userId, // Passa o userId do serviço
                    onDismiss = { showDialog = false }
                )
            }
        }

    }
}

@Composable
fun ServiceItem(serviceName: String, description: String, price: Int, clientName: String, onRequestQuote: () -> Unit) {
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
                text = description,
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "A partir de R$ $price",
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
fun RequestQuoteDialog(serviceName: String, price: Int, clientName: String, userId: String, onDismiss: () -> Unit) {
    var customerMessage by remember { mutableStateOf("") }
    val isSubmitEnabled = customerMessage.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Solicitar Orçamento - $serviceName") },
        text = {
            Column {
                Text("Preço inicial: R$ $price")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = customerMessage,
                    onValueChange = { customerMessage = it },
                    label = { Text("Descreva o que você precisa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Envia a solicitação de orçamento com o userId e clientName
                    sendQuoteRequest(serviceName, customerMessage, userId, clientName)
                    onDismiss()
                },
                enabled = isSubmitEnabled
            ) {
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

// Função para enviar a solicitação de orçamento
fun sendQuoteRequest(serviceName: String, customerMessage: String, userId: String, clientName: String) {
    val db = FirebaseFirestore.getInstance()
    val request = hashMapOf(
        "serviceName" to serviceName,
        "customerMessage" to customerMessage,
        "userId" to userId,
        "clientName" to clientName, // Incluindo o clientName
        "timestamp" to System.currentTimeMillis()
    )
    db.collection("orcamentos").add(request)
        .addOnSuccessListener { println("Solicitação enviada com sucesso.") }
        .addOnFailureListener { println("Erro ao enviar a solicitação: ${it.message}") }
}

// Função para buscar serviços do Firestore
suspend fun fetchServices(): List<ServiceItemModel> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("servicos").get().await()
        snapshot.documents.mapNotNull { it.toServiceItemModel() }
    } catch (e: Exception) {
        emptyList()
    }
}

// Classe de modelo do serviço
data class ServiceItemModel(
    val name: String,
    val description: String,
    val price: Int,
    val userId: String,
    val clientName: String // Adicionando o campo clientName
)

// Extensão para converter documentos do Firestore em ServiceItemModel
fun com.google.firebase.firestore.DocumentSnapshot.toServiceItemModel(): ServiceItemModel? {
    return try {
        ServiceItemModel(
            name = getString("name") ?: "",
            description = getString("description") ?: "",
            price = getLong("price")?.toInt() ?: 0,
            userId = getString("userId") ?: "",
            clientName = getString("clientName") ?: "" // Adicionando o campo clientName
        )
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewServicesScreen() {
    val fakeNavController = rememberNavController()
    ServicesScreen(navController = fakeNavController)
}
