package com.example.timer_quiz.data.repository


import com.example.timer_quiz.data.local.dao.AnswerDao
import com.example.timer_quiz.data.local.dao.GameSessionDao
import com.example.timer_quiz.data.mapper.toDomain
import com.example.timer_quiz.data.mapper.toEntity
import com.example.timer_quiz.domain.model.Answer
import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.model.GameState
import com.example.timer_quiz.domain.repository.GameSessionRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class GameSessionRepositoryImpl(
    private val gameSessionDao: GameSessionDao,
    private val answerDao: AnswerDao,
    private val gson: Gson
) : GameSessionRepository {

    override suspend fun saveGameSession(gameSession: GameSession) {
        val sessionWithId = if (gameSession.id.isEmpty()) {
            gameSession.copy(id = UUID.randomUUID().toString())
        } else {
            gameSession
        }

        gameSessionDao.insertGameSession(sessionWithId.toEntity(gson))

        // Save answers
        sessionWithId.answers.forEach { (_, answer) ->
            answerDao.insertAnswer(answer.toEntity(sessionWithId.id))
        }
    }

    override suspend fun getCurrentGameSession(): GameSession? {
        val sessionEntity = gameSessionDao.getCurrentGameSession() ?: return null
        val answers = answerDao.getAnswersForSession(sessionEntity.id)
        return sessionEntity.toDomain(gson, answers)
    }

    override fun observeGameSession(): Flow<GameSession?> {
        return gameSessionDao.observeCurrentGameSession()
            .map { sessionEntity ->
                if (sessionEntity == null) {
                    null
                } else {
                    // This part needs to be suspendable if answerDao.getAnswersForSession is a suspend function
                    // However, map itself is not designed for suspend functions directly in its transform block.
                    // If getAnswersForSession IS a suspend function, you'll need flatMapLatest or another approach.
                    // Assuming getAnswersForSession is NOT a suspend function for this correction:
                    val answers = answerDao.getAnswersForSession(sessionEntity.id)
                    sessionEntity.toDomain(gson, answers)
                }
            }
    }

    override suspend fun submitAnswer(questionIndex: Int, answer: Answer): Result<Unit> {
        return try {
            val currentSession = getCurrentGameSession()
            if (currentSession != null) {
                answerDao.insertAnswer(answer.toEntity(currentSession.id))

                val updatedAnswers = currentSession.answers.toMutableMap()
                updatedAnswers[questionIndex] = answer

                val updatedScore = updatedAnswers.values.count { it.isCorrect }
                val updatedSession = currentSession.copy(
                    answers = updatedAnswers,
                    score = updatedScore
                )

                saveGameSession(updatedSession)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateGameState(gameState: GameState): Result<Unit> {
        return try {
            val currentSession = getCurrentGameSession()
            if (currentSession != null) {
                val updatedSession = currentSession.copy(gameState = gameState)
                saveGameSession(updatedSession)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun nextQuestion(): Result<GameSession> {
        return try {
            val currentSession = getCurrentGameSession()
            if (currentSession != null) {
                val nextIndex = currentSession.currentQuestionIndex + 1
                val gameState = if (nextIndex >= currentSession.totalQuestions) {
                    GameState.COMPLETED
                } else {
                    GameState.IN_PROGRESS
                }

                val updatedSession = currentSession.copy(
                    currentQuestionIndex = nextIndex,
                    gameState = gameState
                )

                saveGameSession(updatedSession)
                Result.success(updatedSession)
            } else {
                Result.failure(Exception("No current game session"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetGame(): Result<Unit> {
        return try {
            gameSessionDao.deleteAllGameSessions()
            answerDao.deleteAllAnswers()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun pauseGame(): Result<Unit> {
        return updateGameState(GameState.PAUSED)
    }

    override suspend fun resumeGame(): Result<Unit> {
        return updateGameState(GameState.IN_PROGRESS)
    }

    override suspend fun completeGame(): Result<GameSession> {
        return try {
            val currentSession = getCurrentGameSession()
            if (currentSession != null) {
                val completedSession = currentSession.copy(
                    gameState = GameState.COMPLETED,
                    endTime = System.currentTimeMillis()
                )
                saveGameSession(completedSession)
                Result.success(completedSession)
            } else {
                Result.failure(Exception("No current game session"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}