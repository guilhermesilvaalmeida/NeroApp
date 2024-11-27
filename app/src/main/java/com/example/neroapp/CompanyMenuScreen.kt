package com.example.neroapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.RequestQuote // Adicione esta importação
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
fun CompanyMenuScreen(navController: NavController, companyName: String) {
    // Cor principal: Roxo
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu da Empresa", color = Color.White) },
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
            verticalArrangement = Arrangement.Top
        ) {
            Text("Olá $companyName, o que vamos fazer hoje?", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            // Opções da empresa
            Column {
                CompanyMenuOption("Cadastrar Serviço", "Registre um novo serviço oferecido pela sua empresa.", Icons.Filled.Build) {
                    // Navegar para a tela de cadastrar serviço
                }
                Spacer(modifier = Modifier.height(16.dp))
                CompanyMenuOption("Visualizar Serviços", "Veja todos os serviços cadastrados.", Icons.Filled.Visibility) {
                    // Navegar para a tela de visualizar serviços
                }
                Spacer(modifier = Modifier.height(16.dp))
                CompanyMenuOption("Clientes", "Gerencie seus clientes.", Icons.Filled.People) {
                    // Navegar para a tela de clientes
                }
                Spacer(modifier = Modifier.height(16.dp))
                CompanyMenuOption("Visualizar Pedidos de Orçamentos", "Veja todos os pedidos de orçamentos recebidos.", Icons.Filled.RequestQuote) {
                    // Navegar para a tela de visualizar pedidos de orçamentos
                    navController.navigate("sent_quotes") // Navegação para a tela de orçamentos enviados
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Para empurrar o texto para o fundo

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
fun CompanyMenuOption(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
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
                Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)) // Título maior
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), color = Color.Gray) // Descrição
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCompanyMenuScreen() {
    val fakeNavController = rememberNavController()
    CompanyMenuScreen(navController = fakeNavController, companyName = "Empresa XYZ")
}
