package com.example.timer_quiz.presentation.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer_quiz.presentation.ui.components.FlagTitleText
import com.example.timer_quiz.presentation.ui.theme.FlagsColors
import com.example.timer_quiz.presentation.ui.theme.FlagsDimens
import com.example.timer_quiz.presentation.viewmodel.GameOverViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameOverScreen(
    onTimerComplete: (Int) -> Unit,
    viewModel: GameOverViewModel = koinViewModel()
) {
    val remainingTime by viewModel.remainingTime.collectAsState()
    val score by viewModel.score.collectAsState()

    LaunchedEffect(remainingTime) {
        if (remainingTime == 0) {
            onTimerComplete(score)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FlagsColors.CardBackground)
    ) {
        // Orange header bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(FlagsColors.OrangePrimary)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(FlagsDimens.CornerRadius)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(FlagsDimens.CornerRadius)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Header with timer and FLAGS CHALLENGE (same as TimerQuizScreen)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .width(80.dp)
                                .height(52.dp)
                                .background(
                                    FlagsColors.TimerBackground,
                                    RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = String.format("%02d:%02d", remainingTime / 60, remainingTime % 60),
                                color = FlagsColors.TimerText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        FlagTitleText(
                            text = "FLAGS CHALLENGE",
                            fillColor = FlagsColors.OrangePrimary,
                            borderColor = Color.Black,
                            shadowColor = Color.Black,
                            fontSize = 19.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(Modifier.height(80.dp))

                    // Game Over content
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    ) {
                        Text(
                            text = "GAME OVER",
                            color = FlagsColors.TextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}