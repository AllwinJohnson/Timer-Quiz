package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.TimerState
import com.example.timer_quiz.domain.model.TimerType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StartCountdownUseCase {
    operator fun invoke(durationSeconds: Int): Flow<TimerState> = flow {
        val startTime = System.currentTimeMillis()
        var remainingTime = durationSeconds

        while (remainingTime > 0) {
            emit(
                TimerState(
                    isRunning = true,
                    remainingTime = remainingTime,
                    totalTime = durationSeconds,
                    timerType = TimerType.COUNTDOWN,
                    startTime = startTime
                )
            )
            delay(1000L)
            remainingTime--
        }

        emit(
            TimerState(
                isRunning = false,
                remainingTime = 0,
                totalTime = durationSeconds,
                timerType = TimerType.COUNTDOWN,
                startTime = startTime
            )
        )
    }
}