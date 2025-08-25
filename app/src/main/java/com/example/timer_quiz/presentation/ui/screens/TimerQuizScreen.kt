@file:Suppress("FunctionName")

package com.example.timer_quiz.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.timer_quiz.presentation.ui.components.CountryOptionButton
import com.example.timer_quiz.presentation.ui.components.FlagTitleText
import com.example.timer_quiz.presentation.ui.theme.FlagsColors
import com.example.timer_quiz.presentation.ui.theme.FlagsDimens
import com.example.timer_quiz.presentation.viewmodel.TimerQuizViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("DefaultLocale")
@Composable
fun TimerQuizScreen(
    onGameComplete: () -> Unit,
    viewModel: TimerQuizViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val timerState by viewModel.timerState.collectAsState()

    LaunchedEffect(uiState.showGameOver) {
        if (uiState.showGameOver) onGameComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FlagsColors.BackgroundWhite)
    ) {
        // Orange top strip like reference
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
                                text = String.format(
                                    "%02d:%02d",
                                    timerState.remainingTime / 60,
                                    timerState.remainingTime % 60
                                ),
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

                    Spacer(Modifier.height(5.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(30.dp)
                                .clip(RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .offset(x = 4.dp)
                                    .clip(CircleShape)
                                    .background(FlagsColors.OrangePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                val qNum = (uiState.gameSession?.currentQuestionIndex ?: 0) + 1
                                Text(
                                    text = qNum.toString(),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "GUESS THE COUNTRY FROM THE FLAG ?",
                            color = FlagsColors.TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    uiState.currentQuestion?.let { q ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, FlagsColors.BorderLight, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = q.flagUrl,
                                    contentDescription = "Country flag",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(0.dp)
                            ) {
                                Spacer(Modifier.height(16.dp))

                                q.countries.chunked(2).forEach { row ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                                    ) {
                                        row.forEach { country ->
                                            CountryOptionButton(
                                                country = country,
                                                isSelected = uiState.selectedAnswer == country.id,
                                                isCorrect = uiState.showingResult && country.id == q.answerId,
                                                isWrong = uiState.showingResult &&
                                                        uiState.selectedAnswer == country.id &&
                                                        country.id != q.answerId,
                                                showResult = uiState.showingResult,
                                                onClick = { viewModel.selectAnswer(country.id) },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(48.dp)      // slimmer like the mock
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
