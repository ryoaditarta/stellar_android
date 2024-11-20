package com.example.stellar_android.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stellar_android.components.BottomNavBar
import com.example.stellar_android.components.CardJournal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun journalPage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val journals = remember { mutableStateListOf<Map<String, Any>>() }
    val filteredJournals = remember { mutableStateListOf<Map<String, Any>>() } // New list for filtered data
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val selectedDate = remember { mutableStateOf("Today") }
    val context = LocalContext.current

    // Calendar instance for date picker
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    fun filterJournals(selectedDateString: String) {
        val formattedSelectedDate = selectedDateString  // Format the selected date
        val filtered = journals.filter { journal ->
            val createdAt = journal["created_at"] as String
            val createdAtFormatted = formatTimestampToDate(createdAt)
            println(formattedSelectedDate)
            println(createdAtFormatted)
            createdAtFormatted == formattedSelectedDate  // Compare only the date part
        }
        // Clear and add to the filtered journals list
        filteredJournals.clear()
        filteredJournals.addAll(filtered)
    }

    // DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val formattedDate = "${selectedYear}-${selectedMonth+1}-${selectedDay}"
            println(formattedDate)
            val formattedDate2 = formatTimestampToDate(formattedDate)
            selectedDate.value = formattedDate2
            // Filter journals based on the selected date
            filterJournals(formattedDate2)
        },
        year,
        month,
        day
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = "journalPage")
        },
        modifier = Modifier.padding(top = 10.dp),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = it.calculateBottomPadding()) // Use insets padding directly here
                ) {
                    Text(
                        text = "Journals.",
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 2.dp).align(Alignment.CenterHorizontally),
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "What you've written so far",
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp).align(Alignment.CenterHorizontally),
                        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color(0xFF2D2D2D), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)).padding(4.dp)
                            .clickable{datePickerDialog.show()},
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = selectedDate.value,
                            color = Color.White,
                        )
                    }
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        if (journals.isEmpty() && errorMessage.value == null) {
                            Text(
                                text = "Loading journals...",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else if (errorMessage.value != null) {
                            Text(
                                text = "Error: ${errorMessage.value}",
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            // Display filtered journals or all journals if no date is selected
                            val journalsToDisplay = if (filteredJournals.isEmpty()) journals else filteredJournals
                            journalsToDisplay.forEachIndexed { _, journal ->
                                CardJournal(
                                    navController = navController,
                                    title = journal["title"] as? String ?: "No Title",
                                    content = journal["content"] as? String ?: "No Content",
                                    created_at = formatTimestamp(journal["created_at"] as? String),
                                    updated_at = formatTimestamp(journal["updated_at"] as? String),
                                    journalId = journal["journal_id"] as? String ?: "no id"
                                )
                            }
                        }
                    }
                }
            }
        }
    )

    // Fetch journals when the composable is first launched
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            firestore.collection("journal")
                .whereEqualTo("user_id", userId)  // Fetch journals for the current user
                .get()
                .addOnSuccessListener { result ->
                    val fetchedJournals = result.documents.mapNotNull { it.data }
                    journals.clear()
                    journals.addAll(fetchedJournals)
                    // Optionally, initialize filteredJournals if you want to filter by default date
                    filteredJournals.clear()
                    filteredJournals.addAll(journals)  // Initially, show all journals
                }
                .addOnFailureListener { exception ->
                    errorMessage.value = exception.message
                }
        } else {
            errorMessage.value = "No user is currently signed in."
        }
    }
}

// Helper function to format Timestamp to String
fun formatTimestamp(string: String?): String {
    return string ?: "No date"  // Returns an empty string if the input is null
}


// Helper function to format selected date (from DatePicker) into the same format
fun formatTimestampToDate(date: String): String {
    // Split the date into day, month, and year
    val parts = date.split(" ")

    // Handle cases where there might be invalid inpu

    val splisasi = parts[0].split("-")
    val day = splisasi[2]
    val monthIndex = splisasi[1]
    val year = splisasi[0]

    // Define month names
    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    // Validate the month index
    if ( Integer.parseInt(monthIndex) <= 0 || Integer.parseInt(monthIndex) >= monthNames.size) return "Invalid Month"

    // Build the formatted date string
    return "${monthNames[Integer.parseInt(monthIndex)-1]} $day, $year"
}


