package com.example.neroapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Classe que irá conter a lógica de autenticação e interações com o Firebase
class AuthRepository(private val auth: FirebaseAuth) {

    // Função para criar um usuário com email e senha e salvar dados no Firestore
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        cpf: String,
        birthDate: String
    ): String? {
        return try {
            // Cria o usuário no Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            // Após criar o usuário, salva os dados adicionais no Firestore
            val userId = authResult.user?.uid ?: return "Erro ao criar usuário."

            val userMap = mapOf(
                "cpf" to cpf,
                "birthDate" to birthDate
            )

            // Referência ao Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId).set(userMap).await()

            null // Cadastro bem-sucedido
        } catch (e: Exception) {
            e.localizedMessage // Retorna mensagem de erro
        }
    }
}
