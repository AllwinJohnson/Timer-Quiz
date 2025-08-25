@file:Suppress("FunctionName")

package com.example.timer_quiz.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer_quiz.domain.model.Country
import com.example.timer_quiz.presentation.ui.theme.FlagsColors

@Composable
fun CountryOptionButton(
    country: Country,
    isSelected: Boolean,
    isCorrect: Boolean,      // pass true if this option is the answer
    isWrong: Boolean,        // pass true if this is the chosen wrong option
    showResult: Boolean,     // controls reveal of colors/labels
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val success = Color(0xFF20B64A)    // outline green
    val danger  = Color(0xFFD32F2F)    // label red
    val lightBorder = Color(0xFF000000)
    val textDefault = Color(0xFF333333)

    val showCorrect = showResult && isCorrect
    val showWrong   = showResult && isWrong

    val bg = when {
        showWrong   -> FlagsColors.OrangePrimary
        else        -> Color.White
    }
    val border = when {
        showCorrect -> success
        showWrong   -> FlagsColors.OrangePrimary
        else        -> lightBorder
    }
    val textColor = when {
        showWrong   -> Color.White
        else        -> textDefault
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp) // slimmer like the mock
                .clip(RoundedCornerShape(4.dp))
                .background(bg)
                .border(1.dp, border, RoundedCornerShape(4.dp))
                .clickable(enabled = !showResult) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = country.name,
                color = textColor,
                fontSize = 12.sp,                 // reduced text size
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

        if (showCorrect || showWrong) {
            Text(
                text = if (showCorrect) "CORRECT" else "WRONG",
                color = if (showCorrect) success else danger,
                fontSize = 10.sp,                 // small label outside
                fontWeight = FontWeight.Normal
            )
        }
    }
}
