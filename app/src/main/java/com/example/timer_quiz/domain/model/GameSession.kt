package com.example.timer_quiz.domain.model



data class GameSession(
    val id: String = "",
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 15,
    val score: Int = 0,
    val gameState: GameState = GameState.NOT_STARTED,
    val scheduledStartTime: Long? = null,
    val actualStartTime: Long? = null,
    val endTime: Long? = null,
    val answers: Map<Int, Answer> = emptyMap(),
    val timePerQuestion: Int = 30, // seconds
    val breakBetweenQuestions: Int = 10 // seconds
) {
    fun isGameCompleted(): Boolean = currentQuestionIndex >= totalQuestions

    fun getCurrentQuestion(): Question? {
        return if (currentQuestionIndex < questions.size) {
            questions[currentQuestionIndex]
        } else null
    }

    fun getProgress(): Float {
        return if (totalQuestions > 0) {
            currentQuestionIndex.toFloat() / totalQuestions.toFloat()
        } else 0f
    }

    fun getCorrectAnswersCount(): Int {
        return answers.values.count { it.isCorrect }
    }
}