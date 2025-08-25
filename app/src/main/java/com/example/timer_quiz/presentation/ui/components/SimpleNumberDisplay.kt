package com.example.timer_quiz.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleNumberDisplay(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Simple clickable number box - exactly as shown in reference
    Box(
        modifier = modifier
            .size(60.dp, 50.dp)
            .background(
                Color.White,
                RoundedCornerShape(6.dp)
            )
            .border(
                1.dp,
                Color(0xFFE0E0E0), // Light gray border from reference
                RoundedCornerShape(6.dp)
            )
            .clickable {
                // Allow simple increment on click
                onValueChange((value + 1) % 60)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333), // Dark text from reference
            textAlign = TextAlign.Center
        )
    }
}



