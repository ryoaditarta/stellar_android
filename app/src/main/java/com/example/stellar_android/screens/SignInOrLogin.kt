package com.example.stellar_android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellar_android.components.RegistrationButton
import com.example.stellar_android.components.StellarTextComponent

@Composable
fun SignInOrLoginScreen(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)// Ensure background is white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            verticalArrangement = Arrangement.Center // Center vertically
        ) {
            StellarTextComponent(alignment = "Center")
            Spacer(modifier = Modifier.size(16.dp)) // Spacer untuk memberikan ruang vertikal
            RegistrationButton(value = "Login", color = 0xFFB286FD, action = "LogIn", navController = navController)
            Spacer(modifier = Modifier.size(8.dp)) // Spacer kecil untuk memberi jarak antara tombol
            RegistrationButton(value = "Sign Up", color = 0xFFB286FD, action = "SignIn", navController = navController)
        }

    }
}

