package com.example.timer_quiz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.timer_quiz.domain.model.GameState

@Entity(tableName = "game_sessions")
data class GameSessionEntity(
    @PrimaryKey
    val id: String,
    val questionsJson: String, // JSON string of questions
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 15,
    val score: Int = 0,
    val gameState: String = GameState.NOT_STARTED.name,
    val scheduledStartTime: Long? = null,
    val actualStartTime: Long? = null,
    val endTime: Long? = null,
    val timePerQuestion: Int = 30,
    val breakBetweenQuestions: Int = 10,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)