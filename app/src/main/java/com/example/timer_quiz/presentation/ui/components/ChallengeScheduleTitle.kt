package com.example.timer_quiz.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme

@Composable
fun ChallengeScheduleTitle(
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    borderColor: Color = Color.Black,
    fontSize: TextUnit = 22.sp,
    challengeWeight: FontWeight = FontWeight.SemiBold,
    scheduleWeight: FontWeight = FontWeight.ExtraBold,
    cornerRadius: Dp = 4.dp,
    borderWidth: Dp = 2.dp,
    scheduleHorizontalPadding: Dp = 10.dp,
    scheduleVerticalPadding: Dp = 2.dp,
    wordSpacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CHALLENGE",
            color = textColor,
            fontSize = fontSize,
            fontWeight = challengeWeight
        )

        Spacer(Modifier.width(wordSpacing))

        Box(
            modifier = Modifier
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(
                    horizontal = scheduleHorizontalPadding,
                    vertical = scheduleVerticalPadding
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SCHEDULE",
                color = textColor,
                fontSize = fontSize,
                fontWeight = scheduleWeight
            )
        }
    }
}
