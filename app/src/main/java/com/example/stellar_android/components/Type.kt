package com.example.stellar_android.components

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.stellar_android.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val sg = FontFamily(
    Font(R.font.sregular)
)

val sg_bold = FontFamily(Font(R.font.sbold))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = sg_bold,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    bodySmall = TextStyle(
        fontFamily = sg,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    )
)