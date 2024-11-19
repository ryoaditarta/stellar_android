package com.example.stellar_android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.TextField
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun JournalDetail(
    navController: NavController,
    journalId: String, // The journalId passed to the screen
    title: String,
    content: String,
    created_at: String,
    updated_at: String,
) {
    // State untuk menyimpan mode edit dan data yang diubah
    var isEdit by remember { mutableStateOf(false) }
    var editableTitle by remember { mutableStateOf(TextFieldValue(title)) }
    var editableContent by remember { mutableStateOf(TextFieldValue(content)) }
    var editableUpdatedAt by remember { mutableStateOf(updated_at) }

    // Fungsi untuk mendapatkan waktu saat ini dalam format yang diinginkan
    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    // Fungsi untuk mengubah status dan menyimpan data
    fun saveChanges() {
        val db = FirebaseFirestore.getInstance()

        // Create the updated data map
        val updatedData = hashMapOf<String, Any>(
            "title" to editableTitle.text,
            "content" to editableContent.text,
            "updated_at" to getCurrentDateTime() // Update the time of modification
        )

        // Query Firestore to find the document with the matching journalId
        db.collection("journal")
            .whereEqualTo("journal_id", journalId) // Query by the journalId field
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    println("No journal found with the given journalId")
                } else {
                    // If journal found, update the document
                    val document = documents.first() // Since journalId is unique, we take the first document
                    document.reference.update(updatedData)
                        .addOnSuccessListener {
                            // After successful update, navigate back to the journal page
                            navController.navigate("journalPage")
                        }
                        .addOnFailureListener { e ->
                            // Handle the failure
                            println("Error updating document: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure to find document
                println("Error querying document: $e")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Tombol kembali di bagian atas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.navigate("journalPage") }) {
                Icon(imageVector = Icons.Outlined.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            }
            if (isEdit) {
                // Tombol untuk menyimpan perubahan
                Text(
                    text = "Save",
                    color = Color.White,
                    modifier = Modifier.clickable { saveChanges() }
                )
            } else {
                // Tombol untuk mengubah mode ke edit
                Text(
                    text = "Edit",
                    color = Color.White,
                    modifier = Modifier.clickable { isEdit = true }
                )
            }
        }

        // Judul dengan input jika dalam mode edit
        if (isEdit) {
            TextField(
                value = editableTitle,
                onValueChange = { editableTitle = it },
                label = { Text("Title") },
                modifier = Modifier.padding(bottom = 8.dp).background(Color.Black),
                textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )
        } else {
            Text(
                text = editableTitle.text,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Konten dengan input jika dalam mode edit
        if (isEdit) {
            TextField(
                value = editableContent,
                onValueChange = { editableContent = it },
                label = { Text("Content") },
                modifier = Modifier.padding(bottom = 16.dp).background(Color.Black),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        } else {
            Text(
                text = editableContent.text,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Tanggal di pojok kanan bawah
        Spacer(modifier = Modifier.weight(1f)) // Menjaga tanggal di bawah
        Text(
            text = "Created At: $created_at",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = "Updated At: $editableUpdatedAt",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            modifier = Modifier.align(Alignment.End)
        )
    }
}
