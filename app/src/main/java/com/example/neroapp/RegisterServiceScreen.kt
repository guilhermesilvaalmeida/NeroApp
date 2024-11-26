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
fun RegisterServiceScreen() {
    val primaryColor = Color(0xFF6200EE)
    val borderColor = Color(0xFF6200EE) // Cor roxa para a borda
    var serviceName by remember { mutableStateOf(TextFieldValue("")) }
    var servicePrice by remember { mutableStateOf(TextFieldValue("")) }
    var serviceDescription by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Serviço", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Nome do Serviço
            Text("Nome do Serviço", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
            TextField(
                value = serviceName,
                onValueChange = { serviceName = it },
                label = { Text("Digite o nome do serviço") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(2.dp, borderColor), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Preço por Hora
            Text("Preço por Hora (R$)", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
            TextField(
                value = servicePrice,
                onValueChange = { servicePrice = it },
                label = { Text("Digite o preço por hora") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(2.dp, borderColor), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Descrição do Serviço
            Text("Descrição do Serviço", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp))
            TextField(
                value = serviceDescription,
                onValueChange = { serviceDescription = it },
                label = { Text("Digite a descrição do serviço") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(BorderStroke(2.dp, borderColor), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                maxLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Botão de Salvar
            Button(
                onClick = {
                    message = "Serviço '${serviceName.text}' cadastrado com sucesso!"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salvar Serviço", color = Color.White, fontSize = 16.sp)
            }

            // Mensagem de confirmação
            message?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = primaryColor, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterServiceScreen() {
    RegisterServiceScreen()
}
