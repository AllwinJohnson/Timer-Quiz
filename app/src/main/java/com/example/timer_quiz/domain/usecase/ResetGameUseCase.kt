package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.repository.GameSessionRepository

class ResetGameUseCase(
    private val repository: GameSessionRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.resetGame()
    }
}