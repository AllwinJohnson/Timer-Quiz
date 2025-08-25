package com.example.timer_quiz.domain.model

data class TimerState(
    val isRunning: Boolean = false,
    val remainingTime: Int = 0, // seconds
    val totalTime: Int = 0, // seconds
    val timerType: TimerType = TimerType.NONE,
    val startTime: Long? = null
) {
    fun getProgress(): Float {
        return if (totalTime > 0) {
            (totalTime - remainingTime).toFloat() / totalTime.toFloat()
        } else 0f
    }
}

enum class TimerType {
    NONE,
    COUNTDOWN,
    QUESTION,
    BREAK
}