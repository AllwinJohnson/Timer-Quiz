package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.GameSession

class CalculateScoreUseCase {
    operator fun invoke(gameSession: GameSession): Int {
        return gameSession.getCorrectAnswersCount()
    }

    fun calculatePercentage(gameSession: GameSession): Int {
        val totalQuestions = gameSession.totalQuestions
        val correctAnswers = invoke(gameSession)
        return if (totalQuestions > 0) {
            ((correctAnswers.toFloat() / totalQuestions.toFloat()) * 100).toInt()
        } else 0
    }
}