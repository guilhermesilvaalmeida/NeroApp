package com.example.neroapp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetStatusScreen(navController: NavController) {
    val budgets = listOf(
        Budget("Orçamento 1", true),
        Budget("Orçamento 2", false),
        Budget("Orçamento 3", true)
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedBudget by remember { mutableStateOf<Budget?>(null) }
    var description by remember { mutableStateOf("") }

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

            budgets.filter { it.isApproved }.forEach { budget ->
                BudgetCard(budget) {
                    selectedBudget = budget
                    showDialog = true
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Orçamentos Não Aprovados", style = MaterialTheme.typography.headlineLarge)

            budgets.filter { !it.isApproved }.forEach { budget ->
                BudgetCard(budget, false)
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

        if (showDialog) {
            ServiceContractDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    // Lógica para contratar serviço
                    showDialog = false
                },
                budget = selectedBudget,
                description = description,
                onDescriptionChange = { description = it }
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
            Text(budget.name, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            Text(
                if (isApproved) "Aprovado" else "Não Aprovado",
                color = if (isApproved) Color(0xFF4CAF50) else Color.Red,
                fontWeight = FontWeight.Bold
            )
            if (isApproved) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Clique para contratar serviço", color = Color(0xFF6200EE), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceContractDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    budget: Budget?,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Contratar Serviço: ${budget?.name}") },
        text = {
            Column {
                Text("Descreva o que você precisa:", fontWeight = FontWeight.Bold)
                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    placeholder = { Text("Descrição do projeto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF6200EE),
                        focusedIndicatorColor = Color(0xFF6200EE),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                },
                enabled = description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Contratar Serviço", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF6200EE))
            }
        }
    )
}

data class Budget(val name: String, val isApproved: Boolean)

@Preview(showBackground = true)
@Composable
fun PreviewBudgetStatusScreen() {
    val fakeNavController = rememberNavController()
    BudgetStatusScreen(navController = fakeNavController)
}

