package com.example.neroapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Modifier

// Definição da classe OrdemServico
data class OrdemServicoEmpresa(
    val codigo: String,
    val titulo: String,
    val data: String,
    val horario: String,
    var status: String // Alterado para var para permitir atualizações
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceOrderListScreen(navController: NavController) {
    // Cor principal: Roxo
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    // Lista de ordens de serviço simuladas
    val ordensServico = remember { mutableStateListOf(
        OrdemServico("001", "Desenvolvimento de App", "20/09/2024", "14:00", "Aberto"),
        OrdemServico("002", "Manutenção de Sistema", "21/09/2024", "10:00", "Fechado"),
        OrdemServico("003", "Criação de Landing Page", "22/09/2024", "09:30", "Aberto"),
        OrdemServico("004", "Suporte Técnico", "23/09/2024", "11:15", "Fechado")
    ) }

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ordens de Serviço - Empresa", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra de Pesquisa
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Pesquisar") },
                    modifier = Modifier
                        .weight(1f)
                        .border(BorderStroke(1.dp, primaryColor), shape = RoundedCornerShape(8.dp)), // Borda roxa
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = Color.Gray
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = primaryColor
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Ordens de Serviço filtradas
            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.weight(1f) // Adiciona peso para ocupar o espaço restante
            ) {
                items(ordensServico.size) { index ->
                    val ordem = ordensServico[index]

                    if (ordem.titulo.contains(searchQuery, ignoreCase = true)) {
                        QuoteItem(ordem)
                    }
                }
            }

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
fun QuoteItem(ordem: OrdemServico) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Navegar para detalhes ou outra ação */ },
        border = BorderStroke(2.dp, Color(0xFF6200EE)), // Borda roxa
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ticket: ${ordem.codigo}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = ordem.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(text = "Data: ${ordem.data}")
            Text(text = "Horário: ${ordem.horario}")

            // Status com campo ao redor
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(BorderStroke(1.dp, Color(0xFF6200EE)), shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Status: ${ordem.status}",
                    color = Color(0xFF6200EE),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Botões de ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Lógica para entregar o projeto
                        ordem.status = "Entregue"
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Cor padronizada
                ) {
                    Text("Entregar", color = Color.White) // Texto branco para contraste
                }

                Button(
                    onClick = {
                        // Lógica para cancelar o projeto
                        ordem.status = "Cancelado"
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Cor padronizada
                ) {
                    Text("Cancelar", color = Color.White) // Texto branco para contraste
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewServiceOrderListScreen() {
    val fakeNavController = rememberNavController()
    ServiceOrderListScreen(navController = fakeNavController)
}
