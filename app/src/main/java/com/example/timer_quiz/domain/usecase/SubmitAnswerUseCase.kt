package com.example.timer_quiz.domain.usecase

import com.example.timer_quiz.domain.model.Answer
import com.example.timer_quiz.domain.repository.GameSessionRepository

class SubmitAnswerUseCase(
    private val repository: GameSessionRepository
) {
    suspend operator fun invoke(questionIndex: Int, answer: Answer): Result<Unit> {
        return repository.submitAnswer(questionIndex, answer)
    }
}