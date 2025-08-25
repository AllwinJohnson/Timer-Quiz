package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.repository.FlagsChallengeRepository

class ScheduleChallengeUseCase(
    private val repository: FlagsChallengeRepository
) {
    suspend operator fun invoke(scheduledTime: Long): Result<Unit> {
        return try {
            repository.setScheduledTime(scheduledTime)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}