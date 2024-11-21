package com.example.stellar_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CardJournal(
    navController: NavController,
    title: String,
    content: String,
    created_at: String,
    updated_at: String,
    journalId: String
) {
    val formattedDate = formatDate(created_at) // Format the date string

    Box(
        modifier = Modifier
            .padding(5.dp)
            .background(Color(0xff2D2D2D), shape = RoundedCornerShape(8.dp))
            .clickable {
                // Navigate to the JournalDetail screen, passing the journalId as a parameter
                navController.navigate("JournalDetail/$journalId/$title/$content/$created_at/$updated_at")
            }
            .heightIn(min = 150.dp, max = 200.dp) // Set fixed min and max height
            .fillMaxWidth() // Ensure it fills the width equally
    ) {
        // Quote Icon positioned in the top-right corner
        Icon(
            imageVector = Icons.Filled.FormatQuote, // Using the built-in quote icon
            contentDescription = "Quote Icon",
            modifier = Modifier
                .size(60.dp) // Icon size
                .align(Alignment.TopEnd) // Align to the top-right corner
                .padding(15.dp), // Add padding from the corner
            tint = Color(0xFFFFFFFF)
        )

        Column(modifier = Modifier.padding(start = 16.dp, bottom = 28.dp, top = 16.dp, end = 45.dp).align(Alignment.BottomStart)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 24.sp, // Larger font for the title
                modifier = Modifier.padding(bottom = 10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = content,
                color = Color.White,
                fontSize = 16.sp, // Smaller font for the content
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Date in the bottom-right corner
        Text(
            text = formattedDate, // Use formatted date
            color = Color.LightGray,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd), // Align text to the bottom-right corner
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}
fun formatDate(dateString: String): String {
    val parts = dateString.split(" ")

    val splisasi = parts[0].split("-")
    val day = splisasi[2]
    val monthIndex = splisasi[1]
    val year = splisasi[0]

    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    // Validate the month index
    if ( Integer.parseInt(monthIndex) <= 0 || Integer.parseInt(monthIndex) > monthNames.size) return "Invalid Month"

    // Build the formatted date string
    return "${monthNames[Integer.parseInt(monthIndex)-1]} $day, $year"
}

