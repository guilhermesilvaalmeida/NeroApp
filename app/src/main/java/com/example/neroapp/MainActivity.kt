package com.example.neroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neroapp.ui.theme.NeroAppTheme
import com.example.neroapp.SignInScreen
import com.example.neroapp.SignUpScreen
import com.example.neroapp.OrderListScreen
import com.example.neroapp.SettingsScreen
import com.example.neroapp.HomeScreen
import com.example.neroapp.OrderDetailsScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeroAppTheme {
                // Remember NavController
                val navController = rememberNavController()

                // Scaffold for basic layout
                Scaffold {
                    // Navigation host
                    NavHost(navController = navController, startDestination = "signIn") {
                        composable("signIn") {
                            SignInScreen(
                                auth = FirebaseAuth.getInstance(),
                                onNavigateToSignUp = { navController.navigate("signUp") },
                                onSignInSuccess = {
                                    // Navegar para HomeScreen após sucesso no login
                                    navController.navigate("home") {
                                        popUpTo("signIn") { inclusive = true } // Limpar a pilha para evitar voltar ao login
                                    }
                                }
                            )
                        }
                        composable("signUp") {
                            SignUpScreen(
                                auth = FirebaseAuth.getInstance(),
                                onNavigateToSignIn = { navController.popBackStack() }
                            )
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("orderList") {
                            OrderListScreen(navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController)
                        }
                        composable("orderDetails/{orderId}") { backStackEntry ->
                            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                            OrderDetailsScreen(orderId = orderId, navController = navController)
                        }
                        // Adicione mais composables conforme necessário
                    }
                }
            }
        }
    }
}
