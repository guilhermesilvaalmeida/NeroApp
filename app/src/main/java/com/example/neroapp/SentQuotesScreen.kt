package com.example.neroapp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentQuotesScreen(navController: NavController) {
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    // Estado para armazenar os orçamentos recuperados do Firestore
    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obter a instância do Firestore e o ID do usuário logado
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Buscar orçamentos do Firestore
    LaunchedEffect(Unit) {
        if (currentUser != null) {
            try {
                val result: QuerySnapshot = db.collection("orcamentos")
                    .whereEqualTo("userId", currentUser.uid) // Filtrar pelo ID do usuário
                    .get()
                    .await()

                quotes = result.documents.mapNotNull { document ->
                    document.toQuote()
                }
                loading = false
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar orçamentos: ${e.localizedMessage}"
                loading = false
            }
        } else {
            errorMessage = "Erro: Nenhum usuário logado."
            loading = false
        }
    }

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

            when {
                loading -> {
                    // Mostrar indicador de carregamento
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                errorMessage != null -> {
                    // Mostrar mensagem de erro
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                quotes.isEmpty() -> {
                    // Mostrar mensagem de lista vazia
                    Text(
                        text = "Nenhum orçamento encontrado.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    // Exibir lista de orçamentos
                    quotes.forEach { quote ->
                        QuoteItem(quote = quote, db = db) // Passa o db para aprovação
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Desenvolvido por: Enzo e Guilherme",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun QuoteItem(quote: Quote, db: FirebaseFirestore) {
    var showCancelDialog by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .border(2.dp, Color(0xFF6200EE), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Serviço: ${quote.serviceName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Cliente: ${quote.clientName}", fontSize = 16.sp)
            Text(text = "Mensagem do Cliente: ${quote.customerMessage}", fontSize = 16.sp) // Exibe a mensagem do cliente
            Text(text = "Data: ${quote.date}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { approveQuote(quote, db) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Aprovar", color = Color.White)
                }
                Button(
                    onClick = { showCancelDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text("Cancelar", color = Color.White)
                }
            }
        }
    }

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

fun approveQuote(quote: Quote, db: FirebaseFirestore) {
    val quoteRef = db.collection("orcamentos").document(quote.documentId) // Usar documentId para identificar o orçamento específico

    // Atualiza ou cria o campo "aprovado" no Firestore com o valor "true"
    quoteRef.set(mapOf(
        "aprovado" to true
    ), SetOptions.merge()) // Use SetOptions.merge() para não sobrescrever outros campos existentes
        .addOnSuccessListener {
            // Sucesso ao criar ou atualizar o campo "aprovado"
            println("Orçamento aprovado com sucesso")
        }
        .addOnFailureListener { e ->
            // Caso ocorra algum erro
            println("Erro ao aprovar orçamento: ${e.message}")
        }
}

data class Quote(
    val serviceName: String,
    val clientName: String,
    val customerMessage: String, // Usando o campo de mensagem do cliente
    val date: String,
    val userId: String, // userId do usuário que fez o orçamento
    val documentId: String // ID do documento no Firestore
)

fun com.google.firebase.firestore.DocumentSnapshot.toQuote(): Quote? {
    return try {
        Quote(
            serviceName = getString("serviceName") ?: "",
            clientName = getString("clientName") ?: "Não identificado",
            customerMessage = getString("customerMessage") ?: "", // Captura o campo customerMessage
            date = getString("date") ?: "Não identificado",
            userId = getString("userId") ?: "",
            documentId = id // Usando o id do documento como identificador
        )
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSentQuotesScreen() {
    val fakeNavController = rememberNavController()
    SentQuotesScreen(navController = fakeNavController)
}
