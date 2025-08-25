package com.example.timer_quiz.domain.model

enum class GameState {
    NOT_STARTED,
    SCHEDULED,
    COUNTDOWN,
    IN_PROGRESS,
    QUESTION_BREAK,
    SHOWING_RESULT,
    COMPLETED,
    PAUSED
}