package com.example.stellar_android.components

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun handleSignIn(
    nama: String,
    email: String,
    password: String,
    confirmPassword: String,
    navController: NavController,
    errorMessage: MutableState<String>
) {
    val mood = 0
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // Validate passwords
    if (password != confirmPassword) {
        errorMessage.value = "Passwords do not match."
        return
    }

    // Create user with Firebase Authentication
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val userData = hashMapOf(
                    "nama" to nama,
                    "email" to email,
                    "mood" to mood,
                )

                user?.let {
                    // Store user data in Firestore
                    firestore.collection("users").document(it.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            // Navigate to the desired screen after successful registration
                            navController.navigate("SignInOrLoginScreen")
                        }
                        .addOnFailureListener { e ->
                            // Handle Firestore data storage failure
                            errorMessage.value = "Failed to save user data: ${e.localizedMessage}"
                        }
                } ?: run {
                    // Handle unexpected null user
                    errorMessage.value = "User creation successful but user data is null."
                }
            } else {
                // Handle authentication failure
                val exception = task.exception
                errorMessage.value = exception?.localizedMessage ?: "Sign In failed. Please try again."
            }
        }
}
