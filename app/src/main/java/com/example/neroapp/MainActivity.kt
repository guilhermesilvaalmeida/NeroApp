package com.example.neroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()

            // Criação do NavHost
            NavHost(navController, startDestination = "signIn") {
                composable("signIn") {
                    SignInScreen(
                        auth = FirebaseAuth.getInstance(),
                        onNavigateToSignUp = { navController.navigate("signUp") },
                        onNavigateToClientHome = {
                            // Verificar se o usuário é um cliente ou empresa
                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null) {
                                checkIfUserIsCompany(user.email) { isCompany ->
                                    if (isCompany) {
                                        val companyName = "Nome da Empresa" // Pode ser substituído por dados reais
                                        navController.navigate("companyMenu/$companyName")
                                    } else {
                                        // Supondo que o nome do cliente esteja salvo no banco de dados
                                        val clientName = "Nome do Cliente" // Pode ser substituído por dados reais
                                        navController.navigate("clientMenu/$clientName")
                                    }
                                }
                            }
                        },
                        navController = navController
                    )
                }
                composable("signUp") {
                    SignUpScreen(
                        auth = FirebaseAuth.getInstance(),
                        onNavigateToSignIn = { navController.navigate("signIn") }
                    )
                }
                composable("recoverPassword") {
                    RecoverPasswordScreen(navController = navController)
                }

                // Navegação para a tela de Cliente
                composable(
                    route = "clientMenu/{clientName}",
                    arguments = listOf(navArgument("clientName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val clientName = backStackEntry.arguments?.getString("clientName") ?: "Cliente"
                    ClientMenuScreen(navController = navController, clientName = clientName)
                }

                // Navegação para a tela de Empresa
                composable(
                    route = "companyMenu/{companyName}",
                    arguments = listOf(navArgument("companyName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val companyName = backStackEntry.arguments?.getString("companyName") ?: "Empresa"
                    CompanyMenuScreen(navController = navController, companyName = companyName)
                }

                // Navegação para a tela de ClientManagement (Minhas Ordens)
                composable("clientManagement") {
                    ClientManagementScreen(navController = navController)
                }

                // Navegação para a tela de Settings (Minha Conta)
                composable("settings") {
                    SettingsScreen(navController = navController)
                }
                composable("budgetStatus") {
                    BudgetStatusScreen(navController = navController)
                }
                composable("services") {
                    ServicesScreen(navController = navController)
                }
                composable("registerService") {
                    RegisterServiceScreen(navController = navController)
                }
                composable("visualizarServicos") {
                    VisualizarServicosScreen(navController = navController)
                }
                composable("orcamentos") {
                    SentQuotesScreen(navController = navController)
                }
            }
        }
    }

    // Função que verifica se o usuário é uma empresa
    private fun checkIfUserIsCompany(email: String?, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Verificar se o email está na coleção 'empresas'
        db.collection("empresas").whereEqualTo("email", email).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    callback(true) // É uma empresa
                } else {
                    // Verificar na coleção 'clientes' se não for empresa
                    db.collection("clientes").whereEqualTo("email", email).get()
                        .addOnSuccessListener { result ->
                            if (!result.isEmpty) {
                                callback(false) // É um cliente
                            }
                        }
                        .addOnFailureListener { callback(false) }
                }
            }
            .addOnFailureListener { callback(false) }
    }
}
