package com.example.stellar_android.components

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

fun handleLogin(
    email: String,
    password: String,
    navController: NavController,
    errorMessage: MutableState<String>
) {
    if (email.isEmpty() || password.isEmpty()) {
        errorMessage.value = "Please fill in all fields."
        return
    }
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                navController.navigate("Homepage") //
            } else {
                // Login failed, check error type
                val exception = task.exception?.localizedMessage
                errorMessage.value = exception ?: "Login failed. Please check your credentials."
            }
        }
}
