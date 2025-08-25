package com.example.timer_quiz.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer_quiz.presentation.ui.theme.FlagsColors

@Composable
fun ChallengeScheduleScreen(
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FlagsColors.BackgroundWhite)
    ) {
        // Orange header bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(FlagsColors.OrangePrimary)
        )

        // Card container (rounded, blue border like ref)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 2.dp,
                    color = Color(0xFF1E88E5),           // blue outline from reference
                    shape = RoundedCornerShape(10.dp)
                )
                .background(Color.White)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer pill at top-left inside the card
                Box(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                        .background(FlagsColors.TimerBackground)
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "00:10",
                        color = FlagsColors.TimerText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Shadowed title
                Text(
                    text = "FLAGS CHALLENGE",
                    color = FlagsColors.OrangePrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.shadow(2.dp) // subtle title shadow like ref
                )

                Spacer(modifier = Modifier.height(18.dp))

                // CHALLENGE + SCHEDULE tag (dark chip)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHALLENGE ",
                        color = FlagsColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(FlagsColors.TextPrimary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SCHEDULE",
                            color = FlagsColors.ButtonText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Time fields (big square boxes like ref)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimeInputBox(label = "Hour", d1 = "", d2 = "")
                    TimeInputBox(label = "Minute", d1 = "", d2 = "")
                    TimeInputBox(label = "Second", d1 = "", d2 = "")
                }

                Spacer(modifier = Modifier.height(26.dp))

                Button(
                    onClick = onSave,
                    colors = ButtonDefaults.buttonColors(containerColor = FlagsColors.ButtonEnabled),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(
                        text = "Save",
                        color = FlagsColors.ButtonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Purely visual two-digit box + label, matching the large rounded squares in the reference.
 * No logic; you can wire your own state later without changing the look.
 */
@Composable
fun TimeInputBox(label: String, d1: String, d2: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = FlagsColors.TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DigitBox(d1)
            DigitBox(d2)
        }
    }
}

@Composable
private fun DigitBox(value: String) {
    Box(
        modifier = Modifier
            .size(width = 58.dp, height = 56.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF2F2F2))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = FlagsColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
