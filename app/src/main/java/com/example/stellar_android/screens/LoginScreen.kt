package com.example.stellar_android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellar_android.components.LoginButton
import com.example.stellar_android.components.RegistrationButton
import com.example.stellar_android.components.RegistrationTextBox
import com.example.stellar_android.components.StellarTextComponent

@Composable
fun LoginScreen(navController: NavController) {
    // States for holding the input values
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),// Distributes space evenly
            verticalArrangement = Arrangement.Center // Centers horizontally
        ) {
            StellarTextComponent(alignment = "Center")

            // Spacer for visual separation
            Spacer(modifier = Modifier.size(15.dp))

            // Email input box
            RegistrationTextBox(value = "Email", onValueChange = { email.value = it }, false )

            // Password input box
            RegistrationTextBox(value = "Password", onValueChange = { password.value = it }, true)

            // Login button
            LoginButton(
                email = email.value,
                password = password.value,
                navController = navController,
                errorMessage = errorMessage // Passing errorMessage state to LoginButton
            )
            // Sign-in button
            RegistrationButton(
                value = "Sign Up",
                color = 0xFFB286FD,
                action = "SignIn",
                navController = navController
            )
        }
    }
}
