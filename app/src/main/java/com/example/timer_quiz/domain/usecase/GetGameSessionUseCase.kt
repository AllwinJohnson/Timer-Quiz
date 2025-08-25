package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.repository.GameSessionRepository
import kotlinx.coroutines.flow.Flow

class GetGameSessionUseCase(
    private val repository: GameSessionRepository
) {
    fun observeGameSession(): Flow<GameSession?> {
        return repository.observeGameSession()
    }

    suspend fun getCurrentSession(): GameSession? {
        return repository.getCurrentGameSession()
    }
}