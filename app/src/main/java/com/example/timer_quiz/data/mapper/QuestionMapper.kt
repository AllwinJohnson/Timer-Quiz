package com.example.timer_quiz.data.mapper

import com.example.timer_quiz.data.remote.dto.CountryDto
import com.example.timer_quiz.data.remote.dto.QuestionDto
import com.example.timer_quiz.domain.model.Country
import com.example.timer_quiz.domain.model.Question

fun CountryDto.toDomain(): Country {
    return Country(
        id = id,
        name = countryName
    )
}

fun QuestionDto.toDomain(): Question {
    return Question(
        answerId = answerId,
        countries = countries.map { it.toDomain() },
        countryCode = countryCode
    )
}