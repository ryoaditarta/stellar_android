package com.example.stellar_android.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.stellar_android.R // Pastikan ini diimpor untuk akses drawable resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MakeJournalButton(
    navController: NavController,
) {
    var showDialog by remember { mutableStateOf(false) } // To control dialog visibility

    // Show the dialog when the button is clicked
    if (showDialog) {
        MakeJournalDialog(onDismiss = { showDialog = false }, onSave = { title, content ->
            makeJournal(title, content)
            showDialog = false // Close dialog after saving
        })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth() // Card memenuhi lebar layar
            .padding(4.dp) // Padding luar Card
            .clickable { showDialog = true }, // Show dialog on click
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
            // Teks di sebelah kiri
            Text(
                text = "What's your thought?",
                fontSize = 18.sp, // Ukuran teks
                color = Color.White, // Warna teks
                modifier = Modifier.weight(1f) // Memberikan ruang agar teks menyesuaikan
            )

            // Ikon di sebelah kanan
            Image(
                painter = painterResource(id = R.drawable.plan), // Merujuk ikon
                contentDescription = "Icon", // Deskripsi untuk aksesibilitas
                modifier = Modifier.size(35.dp) // Ukuran ikon
            )
        }
    }
}

@Composable
fun MakeJournalDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF2D2D2D),
            modifier = Modifier.padding(16.dp) // Increased padding for larger dialog
        ) {
            Column(
                modifier = Modifier.padding(24.dp), // Increase padding inside the dialog
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create a Journal",
                    fontSize = 26.sp, // Increased font size for the title
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp) // Increased bottom padding
                )

                // Title input field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Title", color = Color(0xFFB286FD)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .height(56.dp), // Increased height for better input experience
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, color = Color.White) // Larger font size for input text
                )

                // Content input field
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(text = "Content", color = Color(0xFFB286FD)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Increased height for better content input area
                        .padding(bottom = 24.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, color = Color.White) // Larger font size for content
                )

                // Save and Cancel buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.White, fontSize = 18.sp) // Larger button text
                    }

                    Button(
                        onClick = {
                            onSave(title.text, content.text)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB286FD)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save", color = Color.White, fontSize = 18.sp) // Larger button text
                    }
                }
            }
        }
    }

}

fun makeJournal(title: String, content: String) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser // Get the current user

    if (user != null) {
        val userId = user.uid // Get the current user's UID

        // Get the current date and format it to "yyyy-MM-dd"
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.format(Date()) // Current date in yyyy-MM-dd format

        // Generate a new journal ID (you can also generate this based on specific logic)
        val journalId = db.collection("journal").document().id
        val createdAt = date
        val updatedAt = date

        // Create a journal document
        val journal = hashMapOf(
            "journal_id" to journalId,
            "title" to title,
            "content" to content,
            "created_at" to createdAt,
            "updated_at" to updatedAt,
            "user_id" to userId // Save the current user's ID
        )

        // Log the journal to check its structure before saving
        println("Journal: $journal")

        // Save the journal in Firestore
        db.collection("journal")
            .document(journalId)
            .set(journal)
            .addOnSuccessListener {
                // Handle success
                println("Journal saved successfully.")
            }
            .addOnFailureListener { e ->
                // Handle error
                println("Error saving journal: ${e.message}")
                e.printStackTrace() // More detailed error information
            }
    } else {
        // If the user is not authenticated, show an error message
        println("User is not authenticated!")
    }
}
