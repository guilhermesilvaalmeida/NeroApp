package com.example.neroapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    // Cor principal: Roxo
    val primaryColor = Color(0xFF6200EE)
    val backgroundColor = Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações", color = Color.White) },
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
            Text(
                text = "Configurações da Conta",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Opções de configuração da conta
            ConfigOptionItem(title = "Alterar Nome de Usuário", onClick = {
                // Aqui, adicione a lógica para alterar o nome
                Toast.makeText(navController.context, "Alterar Nome de Usuário", Toast.LENGTH_SHORT).show()
            })
            ConfigOptionItem(title = "Alterar Senha", onClick = {
                // Aqui, adicione a lógica para alterar a senha
                Toast.makeText(navController.context, "Alterar Senha", Toast.LENGTH_SHORT).show()
            })
            ConfigOptionItem(title = "Excluir Conta", onClick = {
                // Aqui, adicione a lógica para excluir a conta
                Toast.makeText(navController.context, "Excluir Conta", Toast.LENGTH_SHORT).show()
            })

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Configurações de Notificações",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Opções de configuração de notificações
            ConfigOptionItem(title = "Notificações Push", onClick = {
                // Lógica para ativar/desativar notificações push
                Toast.makeText(navController.context, "Notificações Push Ativadas/Desativadas", Toast.LENGTH_SHORT).show()
            })
            ConfigOptionItem(title = "Notificações por E-mail", onClick = {
                // Lógica para ativar/desativar notificações por e-mail
                Toast.makeText(navController.context, "Notificações por E-mail Ativadas/Desativadas", Toast.LENGTH_SHORT).show()
            })

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Configurações de Tema",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Exemplo de switch para mudar o tema (escuro/claro)
            val isDarkMode = remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDarkMode.value = !isDarkMode.value },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isDarkMode.value) "Modo Escuro" else "Modo Claro",
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isDarkMode.value,
                    onCheckedChange = { isDarkMode.value = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Texto final de desenvolvimento
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
fun ConfigOptionItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        border = BorderStroke(1.dp, Color(0xFF6200EE)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Opção de Configuração",
                tint = Color(0xFF6200EE)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    val fakeNavController = rememberNavController()
    SettingsScreen(navController = fakeNavController)
}
