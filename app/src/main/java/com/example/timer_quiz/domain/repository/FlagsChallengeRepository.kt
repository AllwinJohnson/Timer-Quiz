package com.example.timer_quiz.domain.repository

import com.example.timer_quiz.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface FlagsChallengeRepository {
    suspend fun getQuestions(): Result<List<Question>>
    suspend fun getFlagUrl(countryCode: String): String
    fun getScheduledTime(): Flow<Long?>
    suspend fun setScheduledTime(timestamp: Long)
    suspend fun clearScheduledTime()
}