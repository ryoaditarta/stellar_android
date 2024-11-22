package com.example.stellar_android.components

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.stellar_android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.stellar_android.components.Typography

@Composable
fun MakeMoodButton(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) } // State to control dialog visibility

    Card(
        modifier = Modifier
            .fillMaxWidth() // Card memenuhi lebar layar
            .padding(4.dp) // Padding luar Card
            .clickable { showDialog = true }, // Aksi klik untuk membuka dialog
        shape = RoundedCornerShape(12.dp), // Membuat sudut membulat
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D2D) // Warna latar Card
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Row memenuhi lebar Card
                .padding(12.dp), // Padding dalam Card
            verticalAlignment = Alignment.CenterVertically // Elemen rata tengah secara vertikal
        ) {
            Text(
                text = "How's Your Mood?",
                fontSize = 18.sp, // Ukuran teks
                color = Color.White, // Warna teks
                modifier = Modifier.weight(1f), // Memberikan ruang agar teks menyesuaikan
                fontFamily = FontFamily(Font(R.font.sregular))
            )

            Image(
                painter = painterResource(id = R.drawable.moon), // Merujuk ikon
                contentDescription = "Icon", // Deskripsi untuk aksesibilitas
                modifier = Modifier.size(40.dp) // Ukuran ikon
            )
        }
    }

    // Call the makeMood function to show the dialog
    if (showDialog) {
        makeMoodDialog(onDismiss = { showDialog = false })
    }
}

@Composable
fun makeMoodDialog(onDismiss: () -> Unit) {
    var moodValue by remember { mutableStateOf(50f) } // Initial mood value as 50%

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Your Mood",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${moodValue.toInt()}%",
                    fontSize = 30.sp,
                    color = Color(0xFFD286FD)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Slider(
                    value = moodValue,
                    onValueChange = { moodValue = it },
                    valueRange = 0f..100f,
                    modifier = Modifier.height(15.dp), // Make the slider thicker
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFD286FD), // Purple thumb color
                        activeTrackColor = Color(0xFFD286FD), // Purple active track color
                        inactiveTrackColor = Color.White // White inactive track color
                    )
                )

            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Save the mood value to the currentUser's attribute, e.g., using Firebase
                    updateMoodInFirestore(moodValue)
                    onDismiss() // Close the dialog after confirming
                }
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    )
}

fun updateMoodInFirestore(moodValue: Float) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    if (user != null) {
        val userId = user.uid
        db.collection("users").document(userId)
            .update("mood", moodValue)
            .addOnSuccessListener {
                println("Mood updated successfully.")
            }
            .addOnFailureListener { e ->
                println("Error updating mood: ${e.message}")
            }
    }
}
