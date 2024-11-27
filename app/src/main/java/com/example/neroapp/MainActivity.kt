package com.example.neroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

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
                            val clientName = "Cliente XYZ" // Nome fictício do cliente
                            navController.navigate("clientMenu/$clientName")
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
                composable("clientMenu/{clientName}") { backStackEntry ->
                    val clientName = backStackEntry.arguments?.getString("clientName") ?: "Cliente"
                    ClientMenuScreen(navController = navController, clientName = clientName)
                }
            }
        }
    }
}
