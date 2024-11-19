package com.example.stellar_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistrationTextBox(
    value: String,
    onValueChange: (String) -> Unit,
    isSecure: Boolean = false
) {
    val textValue = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(1.dp, Color.Gray, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .padding(12.dp) // Padding inside the text box
    ) {
        BasicTextField(
            value = textValue.value,
            onValueChange = { newValue ->
                textValue.value = newValue
                onValueChange(newValue) // Pass the new value to the onValueChange callback
            },
            modifier = Modifier
                .fillMaxWidth(),
            cursorBrush = SolidColor(Color.Black), // Customize cursor color
            visualTransformation = if (isSecure) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = TextStyle(
                fontSize = 18.sp, // Increase font size
                color = Color.Black // Ensure text is visible
            ),
            decorationBox = { innerTextField ->
                if (textValue.value.isEmpty()) {
                    // Display placeholder when text is empty
                    Text(
                        text = value,
                        color = Color.Gray,
                        fontSize = 18.sp // Match placeholder text size with input text
                    )
                }
                innerTextField() // Render the actual input field
            }
        )
    }
}
