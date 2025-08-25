package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.GameState
import com.example.timer_quiz.domain.repository.GameSessionRepository

class UpdateGameStateUseCase(
    private val repository: GameSessionRepository
) {
    suspend operator fun invoke(gameState: GameState): Result<Unit> {
        return repository.updateGameState(gameState)
    }
}