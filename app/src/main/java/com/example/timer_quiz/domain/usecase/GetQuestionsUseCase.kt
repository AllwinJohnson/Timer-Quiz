package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.Question
import com.example.timer_quiz.domain.repository.FlagsChallengeRepository

class GetQuestionsUseCase(
    private val repository: FlagsChallengeRepository
) {
    suspend operator fun invoke(): Result<List<Question>> {
        return repository.getQuestions()
    }
}