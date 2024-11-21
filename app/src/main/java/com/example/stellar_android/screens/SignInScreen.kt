package com.example.stellar_android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellar_android.components.RegistrationButton
import com.example.stellar_android.components.RegistrationTextBox
import com.example.stellar_android.components.StellarTextComponent
import com.example.stellar_android.components.SignInButton

@Composable
fun SignInScreen(navController: NavController) {
    // State to store user input
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") } // State to store error message

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            StellarTextComponent("Center")

            Spacer(modifier = Modifier.height(16.dp))

            // Name Label and TextField
            RegistrationTextBox(
                value = "Name",
                onValueChange = { name.value = it },
                isSecure = false
            )

            // Email Label and TextField
            Spacer(modifier = Modifier.height(8.dp))
            RegistrationTextBox(
                value = "Email",
                onValueChange = { email.value = it },
                isSecure = false
            )

            // Password Label and TextField
            Spacer(modifier = Modifier.height(8.dp))
            RegistrationTextBox(
                value = "Password",
                onValueChange = { password.value = it },
                isSecure = true
            )

            // Confirm Password Label and TextField
            Spacer(modifier = Modifier.height(8.dp))
            RegistrationTextBox(
                value = "Confirm Password",
                onValueChange = { confirmPassword.value = it },
                isSecure = true
            )

            // Error Message
            if (errorMessage.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Error empty field")
            }

            Spacer(modifier = Modifier.height(16.dp))
            SignInButton(
                name = name.value,
                email = email.value,
                password = password.value,
                confirmPassword = confirmPassword.value,
                navController = navController,
                errorMessage = errorMessage
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Log-In Button
            RegistrationButton(
                value = "Log In",
                color = 0xFFB286FD, // Example purple color
                action = "LogIn",
                navController = navController
            )
        }
    }
}

