package com.example.timer_quiz.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Screen 2: Countdown Screen (Image 2 from beginning)
@Composable
 fun WaitingContent(remainingTime: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            text = "FLAGS CHALLENGE",
            color = Color(0xFFFF6B47),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "WILL START IN",
            color = Color(0xFF333333),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Timer display - exact format from reference
        Text(
            text = String.format("%02d:%02d", remainingTime / 60, remainingTime % 60),
            color = Color(0xFF666666),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}