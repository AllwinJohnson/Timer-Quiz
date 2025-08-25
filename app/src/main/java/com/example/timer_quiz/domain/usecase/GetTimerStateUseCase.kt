package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.TimerState
import com.example.timer_quiz.domain.model.TimerType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTimerStateUseCase {

    /**
     * Starts a question timer for the specified duration
     * Emits TimerState every second until time runs out
     */
    fun startQuestionTimer(durationSeconds: Int): Flow<TimerState> = flow {
        val startTime = System.currentTimeMillis()
        var remainingTime = durationSeconds

        while (remainingTime > 0) {
            emit(
                TimerState(
                    isRunning = true,
                    remainingTime = remainingTime,
                    totalTime = durationSeconds,
                    timerType = TimerType.QUESTION,
                    startTime = startTime
                )
            )
            delay(1000L)
            remainingTime--
        }

        // Emit final state when timer completes
        emit(
            TimerState(
                isRunning = false,
                remainingTime = 0,
                totalTime = durationSeconds,
                timerType = TimerType.QUESTION,
                startTime = startTime
            )
        )
    }

    /**
     * Starts a break timer between questions
     * Emits TimerState every second until break time runs out
     */
    fun startBreakTimer(durationSeconds: Int): Flow<TimerState> = flow {
        val startTime = System.currentTimeMillis()
        var remainingTime = durationSeconds

        while (remainingTime > 0) {
            emit(
                TimerState(
                    isRunning = true,
                    remainingTime = remainingTime,
                    totalTime = durationSeconds,
                    timerType = TimerType.BREAK,
                    startTime = startTime
                )
            )
            delay(1000L)
            remainingTime--
        }

        // Emit final state when break timer completes
        emit(
            TimerState(
                isRunning = false,
                remainingTime = 0,
                totalTime = durationSeconds,
                timerType = TimerType.BREAK,
                startTime = startTime
            )
        )
    }

    /**
     * Helper function to calculate remaining time when app comes back from background
     * This is crucial for maintaining timer accuracy when user backgrounds the app
     */
    fun calculateRemainingTime(
        startTime: Long,
        totalDurationSeconds: Int,
        currentTime: Long = System.currentTimeMillis()
    ): Int {
        val elapsedSeconds = ((currentTime - startTime) / 1000).toInt()
        return maxOf(0, totalDurationSeconds - elapsedSeconds)
    }

    /**
     * Resumes a timer from a specific point
     * Useful for background/foreground scenarios where we need to continue from where we left off
     */
    fun resumeTimer(
        remainingSeconds: Int,
        timerType: TimerType,
        totalSeconds: Int
    ): Flow<TimerState> = flow {
        val resumeStartTime = System.currentTimeMillis()
        var remainingTime = remainingSeconds

        while (remainingTime > 0) {
            emit(
                TimerState(
                    isRunning = true,
                    remainingTime = remainingTime,
                    totalTime = totalSeconds,
                    timerType = timerType,
                    startTime = resumeStartTime
                )
            )
            delay(1000L)
            remainingTime--
        }

        // Emit final state when resumed timer completes
        emit(
            TimerState(
                isRunning = false,
                remainingTime = 0,
                totalTime = totalSeconds,
                timerType = timerType,
                startTime = resumeStartTime
            )
        )
    }

    /**
     * Creates a paused timer state
     * Used when user backgrounds the app or manually pauses
     */
    fun createPausedState(
        remainingTime: Int,
        totalTime: Int,
        timerType: TimerType,
        originalStartTime: Long
    ): TimerState {
        return TimerState(
            isRunning = false,
            remainingTime = remainingTime,
            totalTime = totalTime,
            timerType = timerType,
            startTime = originalStartTime
        )
    }

    /**
     * Validates if a timer should continue running based on elapsed time
     * Returns true if timer should continue, false if it has expired
     */
    fun isTimerValid(
        startTime: Long,
        totalDurationSeconds: Int,
        currentTime: Long = System.currentTimeMillis()
    ): Boolean {
        val elapsedSeconds = ((currentTime - startTime) / 1000).toInt()
        return elapsedSeconds < totalDurationSeconds
    }
}