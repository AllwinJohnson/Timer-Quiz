package com.example.timer_quiz.data.local.dao

import androidx.room.*
import com.example.timer_quiz.data.local.entities.GameSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSessionDao {

    @Query("SELECT * FROM game_sessions ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getCurrentGameSession(): GameSessionEntity?

    @Query("SELECT * FROM game_sessions ORDER BY updatedAt DESC LIMIT 1")
    fun observeCurrentGameSession(): Flow<GameSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameSession(gameSession: GameSessionEntity)

    @Update
    suspend fun updateGameSession(gameSession: GameSessionEntity)

    @Query("DELETE FROM game_sessions")
    suspend fun deleteAllGameSessions()

    @Query("DELETE FROM game_sessions WHERE id = :sessionId")
    suspend fun deleteGameSession(sessionId: String)
}