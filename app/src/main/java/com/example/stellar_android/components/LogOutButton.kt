package com.example.stellar_android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LogoutButton(navController: NavController) {
    // State to track the dialog visibility
    val openDialog = remember { mutableStateOf(false) }

    // Show the logout confirmation dialog when the user clicks "Log Out"
    Box(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { openDialog.value = true },  // Show confirmation dialog
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }

    // Confirmation Dialog
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },  // Dismiss the dialog if the user taps outside
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("SignInOrLoginScreen") {
                            popUpTo("Homepage") { inclusive = true }
                        }
                        openDialog.value = false  // Dismiss the dialog
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog.value = false }  // Dismiss the dialog
                ) {
                    Text("No")
                }
            }
        )
    }
}
