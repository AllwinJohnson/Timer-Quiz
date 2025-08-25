package com.example.timer_quiz.domain.model

data class Answer(
    val questionIndex: Int,
    val selectedCountryId: Int?,
    val correctCountryId: Int,
    val isCorrect: Boolean,
    val timeSpent: Int, // seconds
    val timestamp: Long = System.currentTimeMillis()
)