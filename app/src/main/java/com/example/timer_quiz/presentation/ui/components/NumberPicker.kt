package com.example.timer_quiz.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer_quiz.presentation.ui.theme.FlagsColors

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    // Simple number display box without arrows - matching reference UI
    Box(
        modifier = modifier
            .width(64.dp)
            .height(48.dp)
            .background(
                Color.White,
                RoundedCornerShape(4.dp)
            )
            .border(
                1.dp,
                FlagsColors.BorderLight,
                RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = FlagsColors.TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}
