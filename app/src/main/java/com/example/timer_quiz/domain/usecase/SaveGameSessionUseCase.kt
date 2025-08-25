package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.repository.GameSessionRepository

class SaveGameSessionUseCase(
    private val repository: GameSessionRepository
) {
    suspend operator fun invoke(gameSession: GameSession): Result<Unit> {
        return try {
            repository.saveGameSession(gameSession)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}