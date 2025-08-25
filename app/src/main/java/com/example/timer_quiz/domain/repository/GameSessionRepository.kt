package com.example.timer_quiz.domain.repository

import com.example.timer_quiz.domain.model.Answer
import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.model.GameState
import kotlinx.coroutines.flow.Flow

interface GameSessionRepository {
    suspend fun saveGameSession(gameSession: GameSession)
    suspend fun getCurrentGameSession(): GameSession?
    fun observeGameSession(): Flow<GameSession?>
    suspend fun submitAnswer(questionIndex: Int, answer: Answer): Result<Unit>
    suspend fun updateGameState(gameState: GameState): Result<Unit>
    suspend fun nextQuestion(): Result<GameSession>
    suspend fun resetGame(): Result<Unit>
    suspend fun pauseGame(): Result<Unit>
    suspend fun resumeGame(): Result<Unit>
    suspend fun completeGame(): Result<GameSession>
}