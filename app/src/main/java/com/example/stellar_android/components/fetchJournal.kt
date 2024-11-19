package com.example.stellar_android.components

import com.google.firebase.firestore.FirebaseFirestore

fun fetchJournal(userId: String, onResult: (List<Map<String, Any>>) -> Unit, onError: (Exception) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("journal")
        .whereEqualTo("userId", userId) // Filter by userId
        .get()
        .addOnSuccessListener { result ->
            val journals = result.documents.mapNotNull { it.data }
            onResult(journals) // Return the list of journals
        }
        .addOnFailureListener { exception ->
            onError(exception) // Handle the error
        }
}
