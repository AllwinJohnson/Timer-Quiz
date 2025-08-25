package com.example.timer_quiz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class AnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameSessionId: String,
    val questionIndex: Int,
    val selectedCountryId: Int?,
    val correctCountryId: Int,
    val isCorrect: Boolean,
    val timeSpent: Int,
    val timestamp: Long = System.currentTimeMillis()
)