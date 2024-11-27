package com.example.neroapp

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetStatusScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
println(userId)
    var approvedBudgets by remember { mutableStateOf<List<Budget>>(emptyList()) }
    var unapprovedBudgets by remember { mutableStateOf<List<Budget>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Carregar orçamentos do Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            try {
                // Query budgets in Firestore filtered by userId
                val result: QuerySnapshot = db.collection("orcamentos")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                // Map documents to Budget objects and filter approved/unapproved
                val budgets = result.documents.mapNotNull { document ->
                    document.toBudget()
                }
                approvedBudgets = budgets.filter { it.isApproved }
                unapprovedBudgets = budgets.filter { !it.isApproved }

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
                title = { Text("Status dos Orçamentos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Orçamentos Aprovados", style = MaterialTheme.typography.headlineLarge)

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "Erro desconhecido.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    approvedBudgets.forEach { budget ->
                        BudgetCard(budget) {
                            // Ação ao clicar em um orçamento aprovado
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text("Orçamentos Não Aprovados", style = MaterialTheme.typography.headlineLarge)

                    unapprovedBudgets.forEach { budget ->
                        BudgetCard(budget, false)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Texto de desenvolvimento
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
fun BudgetCard(budget: Budget, isApproved: Boolean = true, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { if (isApproved) onClick() },
        border = BorderStroke(1.dp, Color(0xFF6200EE)), // Borda roxa
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(budget.serviceName, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            Text(
                if (isApproved) "Aprovado" else "Não Aprovado",
                color = if (isApproved) Color(0xFF4CAF50) else Color.Red,
                fontWeight = FontWeight.Bold
            )
            Text("Cliente: ${budget.clientName}", style = MaterialTheme.typography.bodyMedium)
            Text("Mensagem: ${budget.customerMessage}", style = MaterialTheme.typography.bodyMedium)
            Text("Data: ${budget.date}", style = MaterialTheme.typography.bodySmall)

            if (isApproved) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Clique para contratar serviço", color = Color(0xFF6200EE), fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class Budget(
    val serviceName: String,
    val clientName: String,
    val customerMessage: String,
    val date: String,
    val userId: String,
    val isApproved: Boolean
)

fun com.google.firebase.firestore.DocumentSnapshot.toBudget(): Budget? {
    return try {
        Budget(
            serviceName = getString("serviceName") ?: "Sem Nome",
            clientName = getString("clientName") ?: "Não identificado",
            customerMessage = getString("customerMessage") ?: "",
            date = getLong("timestamp")?.let {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
            } ?: "Data não identificada",
            userId = getString("userId") ?: "",
            isApproved = getBoolean("aprovado") ?: false
        )
    } catch (e: Exception) {
        Log.e("Firestore", "Erro ao mapear o documento: ${e.localizedMessage}")
        null
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBudgetStatusScreen() {
    val fakeNavController = rememberNavController()
    BudgetStatusScreen(navController = fakeNavController)
}
