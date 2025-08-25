package com.example.timer_quiz.presentation.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer_quiz.presentation.ui.components.ChallengeScheduleTitle
import com.example.timer_quiz.presentation.ui.components.FlagTitleText
import com.example.timer_quiz.presentation.ui.theme.FlagsColors
import com.example.timer_quiz.presentation.ui.theme.FlagsDimens
import com.example.timer_quiz.presentation.viewmodel.TimerSchedulerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TimerSchedulerScreen(
    onGameStart: () -> Unit,
    viewModel: TimerSchedulerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val timerState by viewModel.timerState.collectAsState()

    LaunchedEffect(uiState.gameStarted) {
        if (uiState.gameStarted) {
            onGameStart()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FlagsColors.BackgroundWhite)
    ) {
        // Orange header bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(FlagsColors.OrangePrimary)
        )

        // Main content based on state
        when {
            uiState.showCountdown -> {
                CountdownContent(timerState.remainingTime)
            }

            uiState.isScheduled -> {
                WaitingContent(uiState.remainingTimeToStart)
            }

            else -> {
                TimerSetupContent(
                    uiState = uiState,
                    onTimeChange = viewModel::setTime,
                    onSave = viewModel::saveScheduledTime
                )
            }
        }

        // Handle errors
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                viewModel.clearError()
            }
        }
    }
}

@Composable
private fun TimerSetupContent(
    uiState: com.example.timer_quiz.presentation.viewmodel.TimerSchedulerState,
    onTimeChange: (Int, Int, Int) -> Unit,
    onSave: () -> Unit
) {
    // Card positioned at top
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
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
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp) // same height as timer box for alignment
            ) {
                // Timer on the left
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart) // stick to left
                        .width(80.dp)
                        .height(52.dp)
                        .background(
                            FlagsColors.TimerBackground,
                            RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "00:10",
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


            Spacer(modifier = Modifier.height(14.dp))

            // CHALLENGE SCHEDULE
            ChallengeScheduleTitle(
                modifier = Modifier.fillMaxWidth(),
                textColor = Color.Black,
                borderColor = Color.Black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Labels: Hour, Minute, Second
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(64.dp)
                ) {
                    Text(
                        text = "Hour",
                        fontSize = 14.sp,
                        color = FlagsColors.TextPrimary
                    )
                    Text(
                        text = "Minute",
                        fontSize = 14.sp,
                        color = FlagsColors.TextPrimary
                    )
                    Text(
                        text = "Second",
                        fontSize = 14.sp,
                        color = FlagsColors.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6 input boxes: HH MM SS (2 boxes each)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Hours - 2 boxes
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SingleDigitInput(
                        value = if (uiState.selectedHours == 0) null else uiState.selectedHours / 10,
                        onValueChange = { tens ->
                            val newHours = ((tens ?: 0) * 10) + (uiState.selectedHours % 10)
                            if (newHours <= 23) {
                                onTimeChange(
                                    newHours,
                                    uiState.selectedMinutes,
                                    uiState.selectedSeconds
                                )
                            }
                        }
                    )
                    SingleDigitInput(
                        value = if (uiState.selectedHours == 0) null else uiState.selectedHours % 10,
                        onValueChange = { ones ->
                            val newHours = (uiState.selectedHours / 10 * 10) + (ones ?: 0)
                            if (newHours <= 23) {
                                onTimeChange(
                                    newHours,
                                    uiState.selectedMinutes,
                                    uiState.selectedSeconds
                                )
                            }
                        }
                    )
                }

                // Minutes - 2 boxes
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SingleDigitInput(
                        value = if (uiState.selectedMinutes == 0) null else uiState.selectedMinutes / 10,
                        onValueChange = { tens ->
                            val newMinutes = ((tens ?: 0) * 10) + (uiState.selectedMinutes % 10)
                            if (newMinutes <= 59) {
                                onTimeChange(
                                    uiState.selectedHours,
                                    newMinutes,
                                    uiState.selectedSeconds
                                )
                            }
                        }
                    )
                    SingleDigitInput(
                        value = if (uiState.selectedMinutes == 0) null else uiState.selectedMinutes % 10,
                        onValueChange = { ones ->
                            val newMinutes = (uiState.selectedMinutes / 10 * 10) + (ones ?: 0)
                            if (newMinutes <= 59) {
                                onTimeChange(
                                    uiState.selectedHours,
                                    newMinutes,
                                    uiState.selectedSeconds
                                )
                            }
                        }
                    )
                }

                // Seconds - 2 boxes
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SingleDigitInput(
                        value = if (uiState.selectedSeconds == 0) null else uiState.selectedSeconds / 10,
                        onValueChange = { tens ->
                            val newSeconds = ((tens ?: 0) * 10) + (uiState.selectedSeconds % 10)
                            if (newSeconds <= 59) {
                                onTimeChange(
                                    uiState.selectedHours,
                                    uiState.selectedMinutes,
                                    newSeconds
                                )
                            }
                        }
                    )
                    SingleDigitInput(
                        value = if (uiState.selectedSeconds == 0) null else uiState.selectedSeconds % 10,
                        onValueChange = { ones ->
                            val newSeconds = (uiState.selectedSeconds / 10 * 10) + (ones ?: 0)
                            if (newSeconds <= 59) {
                                onTimeChange(
                                    uiState.selectedHours,
                                    uiState.selectedMinutes,
                                    newSeconds
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Save button - centered
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onSave,
                    enabled = uiState.isTimeValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FlagsColors.ButtonEnabled,
                        contentColor = FlagsColors.ButtonText,
                        disabledContainerColor = FlagsColors.ButtonDisabled
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(120.dp)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Save",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SingleDigitInput(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember(value) {
        mutableStateOf(if (value == null || value == 0) "" else value.toString())
    }

    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                FlagsColors.BackgroundGray,
                RoundedCornerShape(6.dp)
            )
            .border(
                1.dp,
                FlagsColors.BorderLight,
                RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = { newValue ->
                val filteredValue = newValue.filter { it.isDigit() }.take(1)
                textValue = filteredValue

                val intValue = filteredValue.toIntOrNull()
                if (filteredValue.isEmpty()) {
                    onValueChange(null)
                } else if (intValue != null && intValue <= 9) {
                    onValueChange(intValue)
                }
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = FlagsColors.TextPrimary,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxSize()
        ) { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (textValue.isEmpty()) {
                    Text(
                        text = "0", // Only show "0" as hint
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = FlagsColors.TextTertiary,
                        textAlign = TextAlign.Center
                    )
                }
                innerTextField()
            }
        }
    }
}

@Composable
private fun WaitingContent(remainingTime: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(FlagsDimens.CornerRadius)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp) // same height as timer box for alignment
            ) {
                // Timer on the left
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart) // stick to left
                        .width(80.dp)
                        .height(52.dp)
                        .background(
                            FlagsColors.TimerBackground,
                            RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "00:10",
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "WILL START IN",
                color = FlagsColors.TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = String.format("%02d:%02d", remainingTime / 60, remainingTime % 60),
                color = FlagsColors.TextSecondary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}

@Composable
private fun CountdownContent(remainingTime: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(FlagsDimens.CornerRadius)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp) // same height as timer box for alignment
            ) {
                // Timer on the left
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart) // stick to left
                        .width(80.dp)
                        .height(52.dp)
                        .background(
                            FlagsColors.TimerBackground,
                            RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "00:10",
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "WILL START IN",
                color = FlagsColors.TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = String.format("%02d:%02d", remainingTime / 60, remainingTime % 60),
                color = FlagsColors.TextSecondary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }
}