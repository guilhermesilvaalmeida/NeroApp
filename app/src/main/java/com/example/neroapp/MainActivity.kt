package com.example.neroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "signIn") {
                composable("signIn") {
                    SignInScreen(
                        auth = FirebaseAuth.getInstance(),
                        onNavigateToSignUp = { navController.navigate("signUp") },
                        onNavigateToClientHome = { /* Navegação para a menu do cliente */ },
                        onNavigateToCompanyHome = { /* Navegação para a menu da empresa */ }
                    )
                }
                composable("signUp") {
                    SignUpScreen(
                        auth = FirebaseAuth.getInstance(),
                        onNavigateToSignIn = { navController.navigate("signIn") }
                    )
                }
            }
        }
    }
}
