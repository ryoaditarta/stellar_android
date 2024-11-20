package com.example.stellar_android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.stellar_android.components.BottomNavBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun profilePage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // State untuk menyimpan data pengguna
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userMood by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }

    // Ambil UID pengguna yang login
    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    // Ambil data pengguna berdasarkan UID
    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName = document.getString("nama") ?: "Unknown"
                        userEmail = document.getString("email") ?: "Unknown"
                        userMood = document.getDouble("mood") ?: 0.0
                        isLoading = false
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error fetching user data: $exception")
                    isLoading = false
                }
        } else {
            println("No user logged in")
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = "profilePage")
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
                .background(Color.Black),
            contentAlignment = Alignment.Center // Card di tengah layar
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Card(
                    modifier = Modifier
                        .width(400.dp) // Lebar Card diperbesar
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp), // Padding dalam diperbesar
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {  },
                            modifier = Modifier
                                .size(100.dp) // Ukuran ikon profil lebih besar
                                .clip(CircleShape)
                                .background(Color.Gray)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Add Profile Photo",
                                tint = Color.White,
                                modifier = Modifier.size(60.dp)
                            )
                        }


                        // Nama dan Email
                        Text(
                            text = userName,
                            fontSize = 28.sp, // Font lebih besar
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userEmail,
                            fontSize = 18.sp,
                            color = Color.Gray
                        )

                        // Mood Bar
                        Text(
                            text = "Today's Mood: ${"%.1f".format(userMood)}%",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Icon(
                            imageVector = getMoodIcon(userMood),
                            contentDescription = "Mood Icon",
                            modifier = Modifier
                                .size(60.dp) // Ukuran ikon
                                .padding(bottom = 8.dp),
                            tint = Color.White
                        )
                        LinearProgressIndicator(
                            progress = userMood.toFloat() / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = Color(0xFFD286FD),
                            trackColor = Color.Gray
                        )

                        // Mood Quote
                        Text(
                            text = getMoodQuote(userMood),
                            fontSize = 16.sp,
                            color = Color(0xFFD286FD),
                            fontWeight = FontWeight.Medium
                        )

                        // Tombol Logout
                        Button(
                            onClick = { logout(navController) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD286FD))
                        ) {
                            Text(text = "Logout", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getMoodQuote(mood: Double): String {
    return when {
        mood < 25 -> "Don't give up! Better days are coming. Keep pushing forward, you got this."
        mood < 50 -> "You're doing fine. Stay positive and take small steps to move ahead."
        mood < 75 -> "Great work! You're in a good place. Keep up the momentum."
        else -> "Amazing! Keep shining and spreading positivity. You're an inspiration!"
    }
}

fun logout(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.navigate("loginScreen") // Redirect ke halaman login
}

@Composable
fun getMoodIcon(mood: Double): ImageVector {
    return when {
        mood < 25 -> Icons.Default.SentimentVeryDissatisfied // Sangat sedih
        mood < 50 -> Icons.Default.SentimentDissatisfied     // Sedih
        mood < 75 -> Icons.Default.SentimentNeutral          // Netral
        mood < 90 -> Icons.Default.SentimentSatisfied        // Puas
        else -> Icons.Default.SentimentVerySatisfied         // Sangat bahagia
    }
}
