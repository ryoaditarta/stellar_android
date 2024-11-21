package com.example.stellar_android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Delete
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

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun JournalDetail(
    navController: NavController,
    journalId: String,
    title: String,
    content: String,
    created_at: String,
    updated_at: String,
) {
    var isEdit by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) } // State untuk dialog konfirmasi
    var editableTitle by remember { mutableStateOf(TextFieldValue(title)) }
    var editableContent by remember { mutableStateOf(TextFieldValue(content)) }
    var editableUpdatedAt by remember { mutableStateOf(updated_at) }

    fun saveChanges() {
        val db = FirebaseFirestore.getInstance()
        val updatedData = hashMapOf<String, Any>(
            "title" to editableTitle.text,
            "content" to editableContent.text,
            "updated_at" to getCurrentDate()
        )
        db.collection("journal")
            .whereEqualTo("journal_id", journalId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    println("No journal found with the given journalId")
                } else {
                    val document = documents.first()
                    document.reference.update(updatedData)
                        .addOnSuccessListener {
                            navController.navigate("journalPage")
                        }
                        .addOnFailureListener { e ->
                            println("Error updating document: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error querying document: $e")
            }
    }

    fun deleteJournal() {
        val db = FirebaseFirestore.getInstance()
        db.collection("journal")
            .whereEqualTo("journal_id", journalId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    println("No journal found with the given journalId")
                } else {
                    val document = documents.first()
                    document.reference.delete()
                        .addOnSuccessListener {
                            navController.navigate("journalPage")
                        }
                        .addOnFailureListener { e ->
                            println("Error deleting document: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error querying document: $e")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.navigate("journalPage") }) {
                Icon(imageVector = Icons.Outlined.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            }
            Row(
                modifier = Modifier
                    .padding(8.dp), // Tambahkan sedikit padding agar lebih rapi
                verticalAlignment = Alignment.CenterVertically // Sejajarkan elemen secara vertikal
            ) {
                IconButton(onClick = { showDeleteConfirmation = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp)) // Tambahkan jarak horizontal antara ikon dan teks
                Text(
                    text = if (isEdit) "Save" else "Edit",
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            if (isEdit) saveChanges() else isEdit = true
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp) // Padding untuk teks
                )
            }

        }

        if (isEdit) {
            TextField(
                value = editableTitle,
                onValueChange = { editableTitle = it },
                label = { Text(color = Color(0xFFB286FD), text = "Title", fontWeight = FontWeight.Bold, fontSize = 15.sp)},
                modifier = Modifier
                    .padding(bottom = 8.dp),
                textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF2D2D2D),
                    focusedContainerColor = Color(0xFF2D2D2D),
                    cursorColor = Color(0xFFB286FD),
                )
            )
        } else {
            Text(
                text = editableTitle.text,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        if (isEdit) {
            TextField(
                value = editableContent,
                onValueChange = { editableContent = it },
                label = { Text(color = Color(0xFFB286FD),text = "Content", fontWeight = FontWeight.Bold, fontSize = 15.sp) },
                modifier = Modifier.padding(bottom = 16.dp).background(Color.Black),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF2D2D2D),
                    focusedContainerColor = Color(0xFF2D2D2D),
                    cursorColor = Color(0xFFB286FD),
                )
            )
        } else {
            Text(
                text = editableContent.text,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
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

    // Dialog Konfirmasi Hapus
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Journal") },
            text = { Text("Are you sure you want to delete this journal? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    deleteJournal()
                    showDeleteConfirmation = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
