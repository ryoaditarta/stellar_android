package com.example.stellar_android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StellarTextComponent(alignment: String) {
    // Define box alignment based on input
    val boxAlignment = when (alignment) {
        "Center" -> Alignment.Center
        "TopStart" -> Alignment.TopStart
        "TopCenter" -> Alignment.TopCenter
        "TopEnd" -> Alignment.TopEnd
        "CenterStart" -> Alignment.CenterStart
        "CenterEnd" -> Alignment.CenterEnd
        "BottomStart" -> Alignment.BottomStart
        "BottomCenter" -> Alignment.BottomCenter
        "BottomEnd" -> Alignment.BottomEnd
        else -> Alignment.TopStart // Default alignment
    }

    // Create an AnnotatedString for styled text
    val styledText = buildAnnotatedString {
        // Part 1: Italic "Stella"
        append("Stella")
        addStyle(
            style = SpanStyle(
                fontStyle = FontStyle.Italic,
                fontSize = 50.sp
            ),
            start = 0,
            end = 6
        )
        // Part 2: Regular "r."
        append("r")
        addStyle(
            style = SpanStyle(
                fontSize = 50.sp
            ),
            start = 6,
            end = 7
        )

        append(".")
        addStyle(
            style = SpanStyle(
                fontSize = 55.sp,
                color = Color(0xFFB286FD)
            ),
            start = 7,
            end = 8
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = boxAlignment
    ) {
        Text(
            text = styledText,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}
