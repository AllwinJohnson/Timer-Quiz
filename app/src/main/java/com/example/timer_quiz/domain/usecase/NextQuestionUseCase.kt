package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.repository.GameSessionRepository

class NextQuestionUseCase(
    private val repository: GameSessionRepository
) {
    suspend operator fun invoke(): Result<GameSession> {
        return repository.nextQuestion()
    }
}