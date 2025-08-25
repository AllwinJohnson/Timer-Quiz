package com.example.timer_quiz.presentation.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerDisplay(
    minutes: Int,
    seconds: Int,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = minutes.toString().padStart(2, '0'),
            color = textColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = ":",
            color = textColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Text(
            text = seconds.toString().padStart(2, '0'),
            color = textColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}