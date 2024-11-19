package com.example.stellar_android.screens

import android.graphics.Paint.Align
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stellar_android.R
import com.example.stellar_android.components.BottomNavBar
import com.example.stellar_android.components.MakeJournalButton
import com.example.stellar_android.components.MakeMoodButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun Homepage(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = remember { mutableStateOf("User") }
    val currentDate = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) // Contoh: "November 18, 2024"
        currentDate.value = today
    }
    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName.value = document.getString("nama") ?: "User"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Homepage", "Error: ", exception)
                }
        }
    }

    // Scaffold dengan Bottom Navigation
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = "Homepage")
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Salam & Tanggal
                    Text(
                        text = "Good day, ${userName.value}!",
                        color = Color.White,
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Today is ${currentDate.value}. Let's do something good for yourself.",
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Aksi
                    MakeJournalButton(navController = navController)
                    Spacer(modifier = Modifier.height(8.dp))
                    MakeMoodButton(navController = navController)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Bagian Journal
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Your Journal",
                            color = Color.White,
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Placeholder untuk fetch journal
                        FetchAllJournals(user?.uid ?: "", navController = navController)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Your Snaps",
                            color = Color.White,
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Placeholder untuk fetch journal
                        FetchAllSnaps(user?.uid ?: "", navController = navController)
                    }
                }
            }
        }
    )
}

@Composable
fun FetchAllSnaps(UserId: String, navController: NavController){

}

@Composable
fun FetchAllJournals(userId: String, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val journals = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            db.collection("journal")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val journalList = documents.mapNotNull { it.data }
                    journals.value = journalList
                }
                .addOnFailureListener { exception ->
                    Log.e("FetchAllJournals", "Error fetching journals: ", exception)
                }
        }
    }

    if (journals.value.isEmpty()) {
        Text(
            text = "No journals available.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(top = 8.dp)
                .heightIn(max = 150.dp) // Limit the height of the LazyColumn (adjust as necessary)
        ) {
            items(journals.value.take(3)) { journal ->
                JournalItem(journal = journal, navController = navController)
            }
        }
    }
}


@Composable
fun JournalItem(
    journal: Map<String, Any>,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .background(Color(0xFF2D2D2D), shape = RoundedCornerShape(8.dp))
            .clickable {
                val journalId = journal["journal_id"]?.toString() ?: "Unknown"
                val title = Uri.encode(journal["title"]?.toString() ?: "Untitled")
                val content = Uri.encode(journal["content"]?.toString() ?: "")
                val createdAt = Uri.encode(journal["created_at"]?.toString() ?: "")
                val updatedAt = Uri.encode(journal["updated_at"]?.toString() ?: "")

                navController.navigate(
                    "journalDetail/$journalId/$title/$content/$createdAt/$updatedAt"
                )
            }
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gambar di kiri
        Icon(
            imageVector = Icons.Filled.FormatQuote, // Using the built-in quote icon
            contentDescription = "Quote Icon",
            modifier = Modifier.size(35.dp), // Icon size
            tint = Color(0xFFB286FD) // Tint the icon color to purple (#b286fd)
        )

        // Spacer untuk jarak antara gambar dan teks
        Spacer(modifier = Modifier.width(12.dp))

        // Teks di kanan
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = journal["title"]?.toString() ?: "Untitled",
                color = Color.White,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = journal["content"]?.toString()?.take(30)?.plus("...") ?: "No content",
                color = Color.Gray,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
