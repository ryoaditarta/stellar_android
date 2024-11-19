package com.example.stellar_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegistrationButton(value: String, color: Long, action: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color(color), shape = RoundedCornerShape(16.dp)) // Rounded corners for the Box
            .clickable {
                when (action) {
                    "LogIn" -> {
                        navController.navigate("LoginScreen")
                    }
                    "SignIn" -> {
                        navController.navigate("SignInScreen")
                    }
                }
            }
    ) {
        Text(
            text = value.uppercase(), // Make the text uppercase
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),// Center the text inside the Box
            style = TextStyle(
                fontSize = 18.sp, // Increase font size
                fontWeight = FontWeight.Bold, // Make the font bold
                color = Color.White
            )
        )
    }
}
